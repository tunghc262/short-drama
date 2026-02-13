package com.shortdrama.movie.views.dialogs

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.module.ads.admob.reward.RewardInApp
import com.module.core_api_storage.model_ui.DramaEpisodeUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogEpisodesMovieBinding
import com.shortdrama.movie.utils.UnlockEpisodeManager
import com.shortdrama.movie.views.activities.play_movie.adapter.EpisodesMovieAdapter
import com.shortdrama.movie.views.activities.play_movie.adapter.GenreEpisodesAdapter
import com.shortdrama.movie.views.bases.BaseBottomSheetDialog
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setTextColorById
import com.shortdrama.movie.views.bases.ext.visibleView


class EpisodesMovieDialog(
    val activity: Activity,
    val numberLockMovie: Int,
    val currentEpisodeIndex: Int,
    val listEpisodes: List<DramaEpisodeUIModel>,
    val dramaWithGenresUIModel: DramaWithGenresUIModel,
    val onClickItemEpisode: (Int) -> Unit,
    val onClickUpgrade: () -> Unit,
    val uriPoster: String? = null,
) : BaseBottomSheetDialog<DialogEpisodesMovieBinding>(activity) {

    private var episodesMovieAdapter: EpisodesMovieAdapter? = null

    override fun getLayoutDialog(): Int = R.layout.dialog_episodes_movie

    override fun initViews() {
        super.initViews()
        val bottomSheet = mBinding.root.parent as? View
        bottomSheet?.let { view ->
            val behavior = BottomSheetBehavior.from(view)
            val displayMetrics = activity.resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            val maxHeight = (screenHeight * 0.9).toInt()
            val peekHeight = (screenHeight * 0.6).toInt()
            val layoutParams = view.layoutParams
            layoutParams.height = maxHeight
            view.layoutParams = layoutParams
            behavior.maxHeight = maxHeight
            behavior.peekHeight = peekHeight
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        initData()
        Log.e(
            "MOVIEE",
            "UnlockEpisodeDialog free: ${UnlockEpisodeManager.getRemainingRewards(activity)}",
        )
        if (UnlockEpisodeManager.getRemainingRewards(activity) > 0) {
            RewardInApp.getInstance().loadReward(activity)
        }
    }

    private fun initData() {
        Glide.with(activity).load(uriPoster?.toUri()).into(mBinding.ivBannerMovie)
        mBinding.tvMovieName.text = dramaWithGenresUIModel.dramaUIModel.dramaDescription
        mBinding.tvDes.text = dramaWithGenresUIModel.dramaUIModel.dramaDescription

        val genreAdapter = GenreEpisodesAdapter()
        mBinding.rvGenre.apply {
            adapter = genreAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        dramaWithGenresUIModel.dramaGenresUIModel.let {
            Log.e("MOVIE", "LIST genre ${it.size}: ")
            genreAdapter.submitData(it)
        }

        episodesMovieAdapter = EpisodesMovieAdapter(
            activity,
            numberLockMovie,
            onClickItem = { episodeIndex ->
                onClickItemEpisode(episodeIndex)
                dismiss()
            },
            onClickUpgrade = {
                onClickUpgrade()
            }
        )
        mBinding.rvEpisode.apply {
            setHasFixedSize(true)
            adapter = episodesMovieAdapter
            layoutManager = GridLayoutManager(activity, 5)
        }
        mBinding.rvEpisode.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(
                rv: RecyclerView,
                e: MotionEvent
            ): Boolean {
                rv.parent.requestDisallowInterceptTouchEvent(true)
                return false
            }

            override fun onTouchEvent(
                rv: RecyclerView,
                e: MotionEvent
            ) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        });
        episodesMovieAdapter?.submitListData(
            listEpisodes,
            currentEpisodeIndex,
            dramaWithGenresUIModel
        )
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.llSummary.onClickAlpha {
            unSelectedTab()
            mBinding.apply {
                nsSummaryHand.visibleView()
                ivLineBottomSummary.visibleView()
                mBinding.tvSummary.setTextColorById(R.color.white)
                mBinding.tvSummary.typeface = ResourcesCompat.getFont(activity, R.font.poppins_bold)
            }
        }
        mBinding.llEpisodes.onClickAlpha {
            unSelectedTab()
            mBinding.apply {
                rvEpisode.visibleView()
                ivLineBottomEpisodes.visibleView()
                mBinding.tvEpisodes.setTextColorById(R.color.white)
                mBinding.tvEpisodes.typeface =
                    ResourcesCompat.getFont(activity, R.font.poppins_bold)
            }
        }
    }

    private fun unSelectedTab() {
        mBinding.apply {
            nsSummaryHand.goneView()
            rvEpisode.goneView()
            ivLineBottomEpisodes.goneView()
            ivLineBottomSummary.goneView()
            mBinding.tvSummary.setTextColorById(R.color.white_60)
            mBinding.tvEpisodes.setTextColorById(R.color.white_60)
            mBinding.tvEpisodes.typeface = ResourcesCompat.getFont(activity, R.font.poppins_regular)
            mBinding.tvSummary.typeface = ResourcesCompat.getFont(activity, R.font.poppins_regular)
        }
    }

}