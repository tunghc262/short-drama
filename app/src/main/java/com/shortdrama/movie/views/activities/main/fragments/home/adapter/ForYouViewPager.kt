package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.admob.natives.NativeFullCustom
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.ForYouModel
import com.shortdrama.movie.databinding.ItemAdsForYouBinding
import com.shortdrama.movie.databinding.ItemForYouBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.visibleView

class ForYouViewPager(
    private val activity: Activity,
    val onClickWatchNow: (TVSeriesUiModel) -> Unit,
    val onClickItemSaveMovie: (TVSeriesUiModel) -> Unit,
    val onClickItemUnSaveMovie: (TVSeriesUiModel) -> Unit,
    val onClickItemEpisodes: (TVSeriesUiModel) -> Unit,
    val onClickItemShare: (TVSeriesUiModel) -> Unit
) : PagingDataAdapter<ForYouModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var recyclerView: RecyclerView? = null
    private var player: ExoPlayer? = null
    private var currentPlayingPos = RecyclerView.NO_POSITION
    private val handler = Handler(Looper.getMainLooper())
    private var updateSeekBar: Runnable? = null

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_ADS = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForYouModel>() {
            override fun areItemsTheSame(old: ForYouModel, new: ForYouModel): Boolean {
                return when {
                    old is ForYouModel.Movie && new is ForYouModel.Movie ->
                        old.data.id == new.data.id

                    old is ForYouModel.Ads && new is ForYouModel.Ads -> true
                    else -> false
                }
            }

            override fun areContentsTheSame(old: ForYouModel, new: ForYouModel): Boolean {
                return old == new
            }
        }
    }

    fun getItemAt(position: Int): ForYouModel? = peek(position)

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        recyclerView = null
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ForYouModel.Ads -> TYPE_ADS
            else -> TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADS -> {
                val binding = ItemAdsForYouBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemViewAds(binding)
            }

            else -> {
                val binding = ItemForYouBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ForYouModel.Movie -> (holder as ItemViewHolder).bind(item.data)
            is ForYouModel.Ads -> (holder as ItemViewAds).bind()
            null -> {}
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ItemViewHolder) {
            holder.onRecycled()
        }
    }

    // Helper an toàn để lấy holder đang play
    private fun getCurrentHolder(): ItemViewHolder? {
        if (currentPlayingPos == RecyclerView.NO_POSITION) return null
        return recyclerView?.findViewHolderForAdapterPosition(currentPlayingPos) as? ItemViewHolder
    }

    // ------------------- Item Normal -------------------
    inner class ItemViewHolder(
        val binding: ItemForYouBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TVSeriesUiModel) {
            Glide.with(binding.root)
                .load(item.posterPath)
                .into(binding.ivBannerMovie)

            binding.tvMovieName.text = item.name
            binding.tvOverView.text = item.overview
            binding.playerMoviePreview.player = null

            val genreAdapter = GenreMovieAdapter()
            binding.rvGenre.apply {
                adapter = genreAdapter
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            }
            item.genres?.let { genreAdapter.submitData(it) }

            Glide.with(activity).load(item.backdropPath).into(binding.ivStillLoad)

            if (SharePrefUtils.getBoolean(item.id.toString(), false)) {
                binding.ivFavourite.setImageResource(R.drawable.ic_mark_selected)
            } else {
                binding.ivFavourite.setImageResource(R.drawable.ic_mark)
            }

            setupClicks(item)
        }

        private fun setupClicks(obj: TVSeriesUiModel) {
            binding.llButtonFavourite.onClickAlpha {
                val saved = SharePrefUtils.getBoolean(obj.id.toString(), false)
                SharePrefUtils.putBoolean(obj.id.toString(), !saved)
                binding.ivFavourite.setImageResource(
                    if (saved) R.drawable.ic_mark else R.drawable.ic_mark_selected
                )
                if (saved) onClickItemUnSaveMovie(obj) else onClickItemSaveMovie(obj)
            }

            binding.llButtonWatchFullDrama.onClickAlpha {
                val item = getItem(bindingAdapterPosition)
                if (item is ForYouModel.Movie) {
                    onClickWatchNow(obj)
                }
            }

            binding.llButtonEpisodes.onClickAlpha { onClickItemEpisodes(obj) }
            binding.llButtonShare.onClickAlpha { onClickItemShare(obj) }

            binding.playerMoviePreview.setOnClickListener { togglePlayPause() }

            binding.sbVideo.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (!fromUser) return
                        val duration = player?.duration ?: return
                        val seekTo = duration * progress / 1000
                        player?.seekTo(seekTo)
                        binding.ivPlayPause.goneView()
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        player?.pause()
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        seekBar.thumb = null
                        player?.play()
                    }
                }
            )
        }

        fun onRecycled() {
            binding.playerMoviePreview.player = null
            binding.ivPlayPause.visibility = View.VISIBLE
        }

        private fun togglePlayPause() {
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                    binding.ivPlayPause.visibleView()
                } else {
                    it.play()
                    binding.ivPlayPause.goneView()
                }
            }
        }

        fun showLoading(show: Boolean) {
            binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
            binding.ivPlayPause.visibility = if (show) View.VISIBLE else View.GONE
        }

        fun hiddenStillPath() {
            binding.ivStillLoad.animate()
                .alpha(0f)
                .setDuration(150)
                .withEndAction { binding.ivStillLoad.visibility = View.GONE }
                .start()
        }

        fun viewStillPath() {
            binding.ivStillLoad.visibility = View.VISIBLE
            binding.ivStillLoad.alpha = 1f
        }
    }

    // ------------------- Item Ads -------------------
    inner class ItemViewAds(
        val binding: ItemAdsForYouBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            NativeFullCustom.getInstance().loadAndShow(
                activity,
                binding.lnNative,
                FirebaseQuery.getIdNativeInApp(),
                object : CallbackNative {
                    override fun onLoaded() {
                        binding.root.visibility = View.VISIBLE
                    }

                    override fun onFailed() {
                        Log.e("TAGTAGTAGmnmmnm", "onFailed: ")
                        binding.root.visibility = View.GONE
                    }

                    override fun onAdImpression() {}
                },
                3
            )
        }
    }

    // ------------------- ExoPlayer Controls -------------------
    @OptIn(UnstableApi::class)
    fun playAt(position: Int) {
        if (position == currentPlayingPos) return
        val item = getItem(position) as? ForYouModel.Movie ?: return

        // Stop previous
        if (currentPlayingPos != RecyclerView.NO_POSITION) {
            getCurrentHolder()?.apply {
                binding.playerMoviePreview.player = null
                viewStillPath()
            }
        }

        player?.stop()
        updateSeekBar?.let { handler.removeCallbacks(it) }

        val holder =
            recyclerView?.findViewHolderForAdapterPosition(position) as? ItemViewHolder ?: return

        if (player == null) {
            player = ExoPlayer.Builder(holder.binding.root.context).build().apply {
                repeatMode = ExoPlayer.REPEAT_MODE_ONE
                volume = 1f
                addListener(playerListener)
            }
        }

        val videoUrl = item.data.videos?.firstOrNull()?.link ?: return

        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl))

        holder.binding.playerMoviePreview.player = player
        player?.apply {
            setMediaSource(mediaSource)
            prepare()
        }

        currentPlayingPos = position
        startSeekBarUpdate()
    }

    fun clearPlayItemAds(position: Int) {
        if (position == currentPlayingPos) return
        player?.stop()
        updateSeekBar?.let { handler.removeCallbacks(it) }
    }

    fun play() {
        player?.play()
        getCurrentHolder()?.binding?.ivPlayPause?.goneView()
    }

    fun pause() {
        player?.pause()
        updateSeekBar?.let { handler.removeCallbacks(it) }
        getCurrentHolder()?.binding?.ivPlayPause?.visibleView()
    }

    private fun startSeekBarUpdate() {
        updateSeekBar?.let { handler.removeCallbacks(it) }

        updateSeekBar = object : Runnable {
            override fun run() {
                val duration = player?.duration ?: return
                val position = player?.currentPosition ?: 0L
                val progress = (position * 1000 / duration).toInt()

                getCurrentHolder()?.binding?.sbVideo?.progress = progress
                handler.postDelayed(this, 16)
            }
        }
        handler.post(updateSeekBar!!)
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            val holder = getCurrentHolder() ?: return
            when (state) {
                Player.STATE_BUFFERING -> holder.showLoading(true)
                Player.STATE_READY -> {
                    holder.hiddenStillPath()
                    holder.showLoading(false)
                }

                Player.STATE_ENDED -> holder.viewStillPath()
                Player.STATE_IDLE -> holder.showLoading(false)
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            getCurrentHolder()?.binding?.ivPlayPause?.visibility =
                if (isPlaying) View.GONE else View.VISIBLE
        }

        override fun onPlayerError(error: PlaybackException) {
            getCurrentHolder()?.showLoading(false)
            Log.e("ExoPlayer", error.errorCodeName, error)
        }
    }

    fun releasePlayer() {
        updateSeekBar?.let { handler.removeCallbacks(it) }
        player?.release()
        player = null
        currentPlayingPos = RecyclerView.NO_POSITION
    }
}