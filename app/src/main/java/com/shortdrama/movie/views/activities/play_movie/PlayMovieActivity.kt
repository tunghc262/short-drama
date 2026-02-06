package com.shortdrama.movie.views.activities.play_movie

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
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
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import com.shortdrama.movie.databinding.ActivityPlayMovieBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.utils.ShareUtils
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.GenreMovieAdapter
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.isNetwork
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.showToastByString
import com.shortdrama.movie.views.bases.ext.visibleView
import com.shortdrama.movie.views.dialogs.EpisodesMovieDialog
import com.shortdrama.movie.views.dialogs.PlaySpeedMovieDialog
import com.shortdrama.movie.views.dialogs.ResolutionMovieDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayMovieActivity : BaseActivity<ActivityPlayMovieBinding>() {

    private val viewModel: PlayMovieViewModel by viewModels()
    private var movieModel: TVSeriesUiModel? = null
    private var episodesList: List<EpisodeModel> = listOf()
    private var currentEpisodeIndex: Int = 0
    private var currentEpisode: EpisodeModel? = null
    private var currentResolutionIndex: Int = 0
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
    private val hideHandler = Handler(Looper.getMainLooper())
    private var isControlsVisible = true
    private val hideControlsRunnable = Runnable {
        hideAllControls()
    }

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

        mBinding.playerMovie.onClickAlpha {
            player?.let {
                if (it.isPlaying) {
                    if (isControlsVisible) {
                        it.pause()
                        hideHandler.removeCallbacks(hideControlsRunnable)
                    } else {
                        showAllControls()
                    }
                } else {
                    it.play()
                }
            }
        }

        mBinding.tvSpeed.onClickAlpha {
            PlaySpeedMovieDialog(this, speedId) { speedModel ->
                speedId = speedModel.id
                player?.playbackParameters = PlaybackParameters(speedModel.speed)
                mBinding.tvSpeed.text = speedModel.nameSpeed
            }.show()
        }

        mBinding.sbVideo.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        seekBar.parent.requestDisallowInterceptTouchEvent(true)
                        seekBar.invalidate()
                        val duration = player?.duration ?: return
                        val seekTo = duration * progress / 1000
                        player?.seekTo(seekTo)
                        mBinding.ivPlayPause.goneView()
                        startHideTimer()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    hideHandler.removeCallbacks(hideControlsRunnable)
                    player?.pause()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    player?.play()
                    startHideTimer()
                }
            }
        )

        mBinding.tvQuality.onClickAlpha {
            ResolutionMovieDialog(
                this@PlayMovieActivity,
                currentResolutionIndex
            ) { resolutionMovieModel ->
                currentResolutionIndex = resolutionMovieModel.id
                mBinding.tvQuality.text = resolutionMovieModel.title
                applyResolution(resolutionMovieModel.id)
            }.show()
        }
        mBinding.llButtonFavourite.onClickAlpha {
            movieModel?.let { obj ->
                currentEpisode?.let { currentEpisode ->
                    val watchHistoryEntity = HistoryWatchEntity(
                        id = obj.id,
                        name = obj.name,
                        originalName = obj.originalName,
                        overview = obj.overview,
                        numberOfSeasons = obj.numberOfSeasons,
                        numberOfEpisodes = obj.numberOfEpisodes,
                        posterPath = obj.posterPath,
                        genres = obj.genres,
                        episodeCurrentId = currentEpisode.id,
                        episodeCurrentNo = currentEpisode.episodeNumber,
                        timestamp = System.currentTimeMillis()
                    )
                }
//                movieViewModel.addWatchMovie(watchHistoryEntity)
            }
        }
        mBinding.llButtonEpisodes.onClickAlpha {
            movieModel?.let {
                EpisodesMovieDialog(
                    this@PlayMovieActivity,
                    listEpisodes = episodesList,
                    tvSeriesUiModel = it,
                    onClickItemEpisode = { episodeIndex ->
                        currentEpisodeIndex = episodeIndex
                        currentEpisode = episodesList[episodeIndex]
                        playEpisode(currentEpisodeIndex)
                    },
                    numberLockMovie = FirebaseQuery.getNumberLockMovie().toInt(),
                    currentEpisodeIndex = currentEpisodeIndex
                ).show()
            }
        }
        mBinding.llButtonShareMovie.onClickAlpha {
            ShareUtils.shareApp(this)
        }

        mBinding.llButtonDownloadMovie.onClickAlpha {
            showToastByString("Download success")
        }

    }

    override fun observerData() {
        lifecycleScope.launch {
            viewModel.listEpisodes.collectLatest { response ->
                response?.episodes?.let { list ->
                    episodesList = list
                    val startEpisodeId = intent.getIntExtra(AppConstants.CURRENT_EPISODE_MOVIE, -1)
                    currentEpisodeIndex =
                        list.indexOfFirst { it.id == startEpisodeId }.coerceAtLeast(0)
                    currentEpisode = list[currentEpisodeIndex]
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
            if (isPlaying) {
                // Khi video bắt đầu phát, đợi 2s rồi ẩn controls
                startHideTimer()
            } else {
                // Khi video dừng (Pause), hiện controls lên và không tự ẩn
                hideHandler.removeCallbacks(hideControlsRunnable)
                showAllControls()
            }
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

    private fun showAllControls() {
        if (isControlsVisible) return
        isControlsVisible = true
        val views = listOf(
            mBinding.llToolbar,
            mBinding.llGroupButton1,
            mBinding.clInfo,
            mBinding.sbVideo,
            mBinding.tvSpeed,
            mBinding.tvQuality
        )
        views.forEach { view ->
            view.visibility = View.VISIBLE
            view.animate().alpha(1f).setDuration(300).start()
        }
        startHideTimer()
    }

    private fun hideAllControls() {
        if (player?.isPlaying == false) return
        isControlsVisible = false
        val views = listOf(
            mBinding.llToolbar,
            mBinding.llGroupButton1,
            mBinding.clInfo,
            mBinding.sbVideo,
            mBinding.tvSpeed,
            mBinding.tvQuality
        )
        views.forEach { view ->
            view.animate().alpha(0f).setDuration(300).withEndAction {
                view.visibility = View.GONE
            }.start()
        }
    }

    private fun startHideTimer() {
        hideHandler.removeCallbacks(hideControlsRunnable)
        hideHandler.postDelayed(hideControlsRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateSeekBar?.let { handler.removeCallbacks(it) }
        hideHandler.removeCallbacks(hideControlsRunnable)
        player?.release()
        player = null
    }

}