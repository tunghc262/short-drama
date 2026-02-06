package com.shortdrama.movie.views.activities.play_movie.adapter

import android.app.Activity
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import com.example.core_api.model.core.EpisodeModel
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemContainerEpisodeBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setTextColorById
import com.shortdrama.movie.views.bases.ext.visibleView

class EpisodesMovieAdapter(
    private val activity: Activity,
    val numberLockMovie: Int,
    val onClickItem: (Int) -> Unit
) : BaseRecyclerView<EpisodeModel>() {

    private var currentSelected = 0
    private var tvSeriesUiModel: TVSeriesUiModel? = null
    override fun getItemLayout(): Int = R.layout.item_container_episode

    override fun submitData(newData: List<EpisodeModel>) {}

    fun submitListData(
        newData: List<EpisodeModel>,
        currentSelect: Int,
        tvSeriesUi: TVSeriesUiModel
    ) {
        list.clear()
        list.addAll(newData)
        currentSelected = currentSelect
        tvSeriesUiModel = tvSeriesUi
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: EpisodeModel,
        layoutPosition: Int
    ) {
        if (binding is ItemContainerEpisodeBinding) {
            binding.tvEpisode.text = item.episodeNumber.toString()
            if (layoutPosition == currentSelected) {
                binding.tvEpisode.setTextColorById(R.color.white)
                binding.tvEpisode.typeface =
                    ResourcesCompat.getFont(activity, R.font.poppins_semibold)
                binding.ivPlayEpisode.visibleView()
                binding.root.setBackgroundResource(R.drawable.bg_btn_2a2a2a)
                binding.root.backgroundTintList =
                    ContextCompat.getColorStateList(activity, R.color.purple_200)
            } else {
                binding.tvEpisode.setTextColorById(R.color.white_60)
                binding.tvEpisode.typeface =
                    ResourcesCompat.getFont(activity, R.font.poppins_regular)
                binding.ivPlayEpisode.goneView()
                binding.root.setBackgroundResource(R.drawable.bg_btn_2a2a2a)
            }
            val key = "${tvSeriesUiModel?.id}_${item.id}"
            if (item.episodeNumber >= numberLockMovie && !SharePrefUtils.getBoolean(key, false)) {
                binding.ivLock.visibleView()
            } else {
                binding.ivLock.goneView()
            }
            binding.root.onClickAlpha {
                if (item.episodeNumber >= numberLockMovie && !SharePrefUtils.getBoolean(
                        key,
                        false
                    )
                ) {
                    Log.e("MOVIE", "show : unlockEpisodesDialog")
//                    val unlockEpisodesDialog = UnlockEpisodesDialog(activity, onClickWatchAds = {
//                        RewardInApp.getInstance().showReward(activity, object : CallbackAd {
//                            override fun onNextAction() {
//                                SharePrefUtils.putBoolean(key, true)
//                                onClickItem(item.id)
//                            }
//                        })
//                    })
//                    unlockEpisodesDialog.show()
                } else {
                    setSelectedItem(layoutPosition)
                    onClickItem(layoutPosition)
                }
            }
        }
    }

    fun setSelectedItem(position: Int) {
        notifyItemChanged(currentSelected)
        notifyItemChanged(position)
        currentSelected = position
    }
}