package com.shortdrama.movie.views.activities.play_movie.adapter

import android.app.Activity
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import com.example.core_api.model.core.EpisodeModel
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.admob.reward.RewardInApp
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemContainerEpisodeBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setTextColorById
import com.shortdrama.movie.views.bases.ext.showToastByString
import com.shortdrama.movie.views.bases.ext.visibleView
import com.shortdrama.movie.views.dialogs.UnlockEpisodeDialog
import kotlin.math.max

class EpisodesMovieAdapter(
    private val activity: Activity,
    val numberLockMovie: Int,
    val onClickItem: (Int) -> Unit,
    val onClickUpgrade: () -> Unit
) : BaseRecyclerView<EpisodeModel>() {

    private var currentSelected = 0
    private var tvSeriesUiModel: TVSeriesUiModel? = null

    private var maxUnlockedEpisode: Int = 0
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
            val isLocked = item.episodeNumber > numberLockMovie && !SharePrefUtils.getBoolean(
                key,
                false
            ) && FirebaseQuery.getEnableAds() && !PurchaseUtils.isNoAds(activity)
            val prevEpisodeNumber = item.episodeNumber - 1
            val prevKey =
                "${tvSeriesUiModel?.id}_${prevEpisodeNumber}"  // Giả sử id episode là episodeNumber, nếu không thì cần map đúng id
            val prevUnlocked = prevEpisodeNumber < 1 || SharePrefUtils.getBoolean(prevKey, false)
            if (isLocked) {
                binding.ivLock.visibleView()
            } else {
                maxUnlockedEpisode = max(maxUnlockedEpisode, layoutPosition)
                binding.ivLock.goneView()
            }
            binding.root.onClickAlpha {
                if (isLocked) {
                    if ((layoutPosition - 1 == maxUnlockedEpisode)) {
                        Log.e("MOVIE", "show : unlockEpisodesDialog")
                        val unlockEpisodesDialog = UnlockEpisodeDialog(
                            activity,
                            onClickWatchAds = {
                                RewardInApp.getInstance().showReward(activity) {
                                    SharePrefUtils.putBoolean(key, true)
                                    setSelectedItem(layoutPosition)
                                    onClickItem(layoutPosition)
                                }
                            },
                            onClickUpgrade = {
                                onClickUpgrade()
                            }
                        )
                        unlockEpisodesDialog.show()
                    } else {
                        activity.showToastByString("Please unlock episodes in sequential order!")
                    }
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