package com.shortdrama.movie.views.activities.play_movie

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.module.ads.remote.FirebaseQuery
import com.module.core_api_storage.model_ui.DramaEpisodeUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import com.shortdrama.movie.databinding.ActivityPlayMovieBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.utils.ShareUtils
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.GenreMovieAdapter
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.FavoriteViewModel
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.HistoryViewModel
import com.shortdrama.movie.views.activities.premium.PremiumActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.showToastByString
import com.shortdrama.movie.views.bases.ext.visibleView
import com.shortdrama.movie.views.dialogs.EpisodesMovieDialog
import com.shortdrama.movie.views.dialogs.PlaySpeedMovieDialog
import com.shortdrama.movie.views.dialogs.RemoveFavouriteDialog
import com.shortdrama.movie.views.dialogs.ResolutionMovieDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@UnstableApi
@AndroidEntryPoint
class PlayMovieActivity : BaseActivity<ActivityPlayMovieBinding>() {

    private val viewModel: PlayMovieViewModel by viewModels()
    private val favouriteViewModel: FavoriteViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    private var uriPoster: String? = null
    private var movieModel: DramaWithGenresUIModel? = null
    private var episodesList: MutableList<DramaEpisodeUIModel> = mutableListOf()
    private var currentEpisodeIndex: Int = 0
    private var currentEpisode: DramaEpisodeUIModel? = null
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
        Log.e("MOVIEEE", "initViews: 1")
        genreMovieAdapter = GenreMovieAdapter()
        trackSelector = DefaultTrackSelector(this)
        intent?.let {
            if (it.hasExtra(AppConstants.OBJ_MOVIE)) {
                Log.e("MOVIEEE", "initViews: 2")
                movieModel = it.getParcelableExtra(AppConstants.OBJ_MOVIE)
                if (movieModel != null) {
                    Log.e("MOVIEEE", "initViews: 3")
                    movieModel?.dramaUIModel?.dramaId?.let { dramaId ->
                        Log.e("MOVIEEE", "initViews: 4")
                        viewModel.loadEpisodes(
                            dramaId
                        )
                    }
                }
                Log.e("MOVIEEE", "initViews: ${movieModel?.dramaUIModel?.dramaId}")
            }
        }
        setupPlayer()
        mBinding.rvGenre.apply {
            adapter = genreMovieAdapter
            layoutManager =
                LinearLayoutManager(this@PlayMovieActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        genreMovieAdapter?.submitData(movieModel?.dramaGenresUIModel ?: emptyList())
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
                val favouriteMovieEntity = MovieFavoriteEntity(
                    dramaId = obj.dramaUIModel.dramaId,
                    name = obj.dramaUIModel.dramaName,
                    description = obj.dramaUIModel.dramaDescription,
                    thumb = obj.dramaUIModel.dramaThumb,
                    totalEpisode = obj.dramaUIModel.totalEpisode,
                    dramaTrailer = obj.dramaUIModel.dramaTrailer,
                    genresJson = Gson().toJson(obj.dramaGenresUIModel),
                )
                if (SharePrefUtils.getBoolean(obj.dramaUIModel.dramaId, false)) {
                    RemoveFavouriteDialog(this) {
                        mBinding.ivFavourite.setImageResource(R.drawable.ic_mark)
                        SharePrefUtils.putBoolean(obj.dramaUIModel.dramaId, false)
                        favouriteViewModel.removeFromFavourite(favouriteMovieEntity.dramaId)
                    }.show()
                } else {
                    mBinding.ivFavourite.setImageResource(R.drawable.ic_mark_selected)
                    SharePrefUtils.putBoolean(obj.dramaUIModel.dramaId, true)
                    favouriteViewModel.addToFavourite(favouriteMovieEntity)
                }
            }
        }
        mBinding.llButtonEpisodes.onClickAlpha {
            movieModel?.let {
                EpisodesMovieDialog(
                    this@PlayMovieActivity,
                    listEpisodes = episodesList,
                    dramaWithGenresUIModel = it,
                    onClickItemEpisode = { episodeIndex ->
                        currentEpisodeIndex = episodeIndex
                        currentEpisode = episodesList[episodeIndex]
                        playEpisode(currentEpisodeIndex)
                    },
                    numberLockMovie = FirebaseQuery.getNumberLockMovie().toInt(),
                    currentEpisodeIndex = currentEpisodeIndex,
                    onClickUpgrade = {
                        val intent = Intent(this, PremiumActivity::class.java)
                        startActivity(intent)
                    },
                    uriPoster = uriPoster
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listEpisodes.collectLatest { list ->
                    Log.e("MOVIEEE", "observerData list episode ${list.size}: ")
                    if (list.isNotEmpty()) {
                        episodesList.clear()
                        episodesList.addAll(list)
                        val startEpisodeId =
                            intent.getStringExtra(AppConstants.CURRENT_EPISODE_MOVIE_ID)
                        currentEpisodeIndex =
                            list.indexOfFirst { it.episodeId == startEpisodeId }.coerceAtLeast(0)
                        currentEpisode = list[currentEpisodeIndex]
                        playEpisode(currentEpisodeIndex)
                    }
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
            Log.d(
                "StorageSuccess", when (state) {
                    Player.STATE_IDLE -> "IDLE"
                    Player.STATE_BUFFERING -> "BUFFERING"
                    Player.STATE_READY -> "READY"
                    Player.STATE_ENDED -> "ENDED"
                    else -> "UNKNOWN"
                }
            )
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
        Log.e("MOVIEEE", "playEpisode: 1")
        if (index !in episodesList.indices) return
        Log.e("MOVIEEE", "playEpisode: 2")
        currentEpisodeIndex = index
        val episode = episodesList[index]
        updateUI(episode)
        StorageSource.getStorageDownloadUrl(
            episode.urlStream,
            onSuccess = { downloadUrl ->
                Log.d("MOVIEEE", "URL video: $downloadUrl")
                val uri = Uri.parse(downloadUrl)
                val mediaItem = MediaItem.fromUri(uri)
                player?.apply {
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = true
                    play()
                }
                // Reset history
                isSavedHistory = false
                watchTime = 0L
                lastPosition = 0L
                startSeekBarUpdate()
            },
            onError = { error ->
                showToastByString("Không tải được video")
                Log.e("MOVIEEE", "Path: ${episode.urlStream}", error)
            }
        )
    }

    private fun updateUI(episode: DramaEpisodeUIModel) {
        mBinding.tvTitle.text = "EP.${episode.serialNo} - ${movieModel?.dramaUIModel?.dramaName}"
        mBinding.tvMovieName.text = movieModel?.dramaUIModel?.dramaName
        movieModel?.dramaUIModel?.dramaThumb?.let {
            StorageSource.getStorageDownloadUrl(
                it,
                onSuccess = { uri ->
                    Log.d("MOVIEEE", "URL thumb: $uri")
                    uriPoster = uri
                    Glide.with(this).load(uri).into(mBinding.ivBannerMovie)
                },
                onError = {
                    Log.e("MOVIEEE", "bindData: onError")
                })
        }
        mBinding.ivStillLoad.visibleView()
        val isFav = SharePrefUtils.getBoolean(movieModel?.dramaUIModel?.dramaId.toString(), false)
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
                        if (!isSavedHistory && watchTime >= 5_000L) {
                            saveToHistory()
                            isSavedHistory = true
                        }
                    }
                }
                handler.postDelayed(this, 1000L)
            }
        }
        handler.post(updateSeekBar!!)
    }

    private fun saveToHistory() {
        isSavedHistory = true
        movieModel?.let {
            Log.e("MOVIE", "saveToHistory: ${it.dramaUIModel.dramaId}")
            val currentEp = episodesList[currentEpisodeIndex]
            val history = HistoryWatchEntity(
                dramaId = it.dramaUIModel.dramaId,
                name = it.dramaUIModel.dramaName,
                description = it.dramaUIModel.dramaDescription,
                thumb = it.dramaUIModel.dramaThumb,
                totalEpisode = it.dramaUIModel.totalEpisode,
                dramaTrailer = it.dramaUIModel.dramaTrailer,
                genresJson = Gson().toJson(it.dramaGenresUIModel),
                episodeId = currentEp.episodeId,
                episodeNo = currentEp.serialNo,
            )
            historyViewModel.addToWatchHistory(history)
        }
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
            mBinding.tvQuality,
            mBinding.viewOverlay
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
            mBinding.tvQuality,
            mBinding.viewOverlay
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
        Log.e("MOVIEE", "onDestroy: ")
        player?.release()
        player = null
    }

}