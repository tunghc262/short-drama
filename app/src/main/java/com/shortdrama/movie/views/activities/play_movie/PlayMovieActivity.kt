package com.shortdrama.movie.views.activities.play_movie

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core_api.model.core.EpisodeModel
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.remote.FirebaseQuery
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivityPlayMovieBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.GenreMovieAdapter
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.isNetwork
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.showToastByString
import com.shortdrama.movie.views.bases.ext.visibleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayMovieActivity : BaseActivity<ActivityPlayMovieBinding>() {

    private val viewModel: PlayMovieViewModel by viewModels()
    private var movieModel: TVSeriesUiModel? = null
    private var episodesList: List<EpisodeModel> = listOf()
    private var currentEpisodeIndex: Int = 0

    private var player: ExoPlayer? = null
    private lateinit var trackSelector: DefaultTrackSelector
    private val handler = Handler(Looper.getMainLooper())
    private var updateSeekBar: Runnable? = null

    private var genreMovieAdapter: GenreMovieAdapter? = null

    // History & State
    private var lastPosition = 0L
    private var watchTime = 0L
    private var isSavedHistory = false
    private var speedId: Int = 2 // Default 1x

    override fun getLayoutActivity(): Int = R.layout.activity_play_movie

    override fun initViews() {
        super.initViews()
        genreMovieAdapter = GenreMovieAdapter()
        trackSelector = DefaultTrackSelector(this)
        intent?.let {
            if (it.hasExtra(AppConstants.OBJ_MOVIE)) {
                movieModel = it.getParcelableExtra(AppConstants.OBJ_MOVIE)
                Log.e("MOVIE", "initViews: ${movieModel?.id}")
            }
        }

        if (isNetwork()) {
            movieModel?.let { obj ->
                obj.numberOfSeasons?.let { season ->
                    viewModel.loadEpisodes(obj.id, season)
                }
            }
        }
        setupPlayer()
        mBinding.rvGenre.apply {
            adapter = genreMovieAdapter
            layoutManager =
                LinearLayoutManager(this@PlayMovieActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        genreMovieAdapter?.submitData(movieModel?.genres ?: emptyList())
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivBack.onClickAlpha { finish() }
    }

    override fun observerData() {
        lifecycleScope.launch {
            viewModel.listEpisodes.collectLatest { response ->
                response?.episodes?.let { list ->
                    episodesList = list
                    val startEpisodeId = intent.getIntExtra(AppConstants.CURRENT_EPISODE_MOVIE, -1)
                    currentEpisodeIndex = list.indexOfFirst { it.id == startEpisodeId }.coerceAtLeast(0)
                    playEpisode(currentEpisodeIndex)
                }
            }
        }
    }
    private fun setupPlayer() {
        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build().apply {
                repeatMode = Player.REPEAT_MODE_OFF
                addListener(playerListener)
            }
        mBinding.playerMovie.player = player
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            mBinding.pbLoading.visibility =
                if (state == Player.STATE_BUFFERING) View.VISIBLE else View.GONE
            if (state == Player.STATE_READY) {
                mBinding.ivStillLoad.goneView()
            }
            if (state == Player.STATE_ENDED) {
                if (currentEpisodeIndex < episodesList.size - 1) {
                    playEpisode(currentEpisodeIndex + 1)
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            mBinding.ivPlayPause.visibility = if (isPlaying) View.GONE else View.VISIBLE
        }

        override fun onPlayerError(error: PlaybackException) {
            showToastByString("Error playing video")
        }
    }

    private fun playEpisode(index: Int) {
        if (index !in episodesList.indices) return
        currentEpisodeIndex = index
        val episode = episodesList[index]
        // 1. Check Unlock
        val key = "${movieModel?.id}_${episode.id}"
        val numberLockMovie = FirebaseQuery.getNumberLockMovie()
        if (episode.episodeNumber >= numberLockMovie && !SharePrefUtils.getBoolean(key, false)) {
            player?.pause()
//            showUnlockDialog(episode, key)
            return
        }
        // 2. Update UI
        updateUI(episode)
        // 3. Prepare Media
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(episode.link))

        player?.apply {
            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true
            // Reset history tracking
            isSavedHistory = false
            watchTime = 0L
            lastPosition = 0L
        }
        startSeekBarUpdate()
    }

    private fun updateUI(episode: EpisodeModel) {
        mBinding.tvTitle.text = "EP.${episode.episodeNumber} - ${movieModel?.name}"
        mBinding.tvMovieName.text = movieModel?.name
        Glide.with(this).load(movieModel?.posterPath).into(mBinding.ivBannerMovie)
        Glide.with(this).load(episode.stillPath).into(mBinding.ivStillLoad)
        mBinding.ivStillLoad.visibleView()
        val isFav = SharePrefUtils.getBoolean(movieModel?.id.toString(), false)
        mBinding.ivFavourite.setImageResource(if (isFav) R.drawable.ic_mark_selected else R.drawable.ic_mark)
    }

    private fun startSeekBarUpdate() {
        updateSeekBar?.let { handler.removeCallbacks(it) }
        updateSeekBar = object : Runnable {
            override fun run() {
                player?.let {
                    val duration = it.duration
                    val position = it.currentPosition
                    if (duration > 0) {
                        mBinding.sbVideo.progress = (position * 1000 / duration).toInt()

                        if (it.isPlaying) {
                            if (lastPosition > 0) watchTime += (position - lastPosition)
                            lastPosition = position
                        }
                        if (!isSavedHistory && watchTime >= 10_000L) {
//                            saveToHistory()
                        }
                    }
                }
                handler.postDelayed(this, 1000L)
            }
        }
        handler.post(updateSeekBar!!)
    }

    private fun applyResolution(id: Int) {
        val builder = trackSelector.buildUponParameters()
        when (id) {
            0 -> builder.clearVideoSizeConstraints() // Auto
            1 -> builder.setMaxVideoSize(1920, 1080).setMinVideoSize(1920, 1080)
            2 -> builder.setMaxVideoSize(1280, 720).setMinVideoSize(1280, 720)
            3 -> builder.setMaxVideoSize(854, 480).setMinVideoSize(854, 480)
        }
        trackSelector.setParameters(builder.build())
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateSeekBar?.let { handler.removeCallbacks(it) }
        player?.release()
        player = null
    }

}