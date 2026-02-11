package com.shortdrama.movie.views.activities.play_movie.adapter

import android.app.Activity
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import com.module.ads.admob.reward.RewardInApp
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.module.core_api_storage.model_ui.DramaEpisodeUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
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
) : BaseRecyclerView<DramaEpisodeUIModel>() {

    private var currentSelected = 0
    private var tvSeriesUiModel: DramaWithGenresUIModel? = null

    private var maxUnlockedEpisode: Int = 0
    override fun getItemLayout(): Int = R.layout.item_container_episode

    override fun submitData(newData: List<DramaEpisodeUIModel>) {}

    fun submitListData(
        newData: List<DramaEpisodeUIModel>,
        currentSelect: Int,
        tvSeriesUi: DramaWithGenresUIModel
    ) {
        list.clear()
        list.addAll(newData)
        currentSelected = currentSelect
        tvSeriesUiModel = tvSeriesUi
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: DramaEpisodeUIModel,
        layoutPosition: Int
    ) {
        if (binding is ItemContainerEpisodeBinding) {
            binding.tvEpisode.text = item.serialNo
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
            val key = item.episodeId
            Log.e("MOVIEEE", "numberLockMovie: $numberLockMovie")
            Log.e(
                "MOVIEEE", "key: ${
                    !SharePrefUtils.getBoolean(
                        key,
                        false
                    )
                }"
            )
            val isLocked = item.serialNo.toInt() > numberLockMovie && !SharePrefUtils.getBoolean(
                key,
                false
            ) && FirebaseQuery.getEnableAds() && !PurchaseUtils.isNoAds(activity)
            if (isLocked) {
                binding.ivLock.visibleView()
            } else {
                maxUnlockedEpisode = max(maxUnlockedEpisode, layoutPosition)
                binding.ivLock.goneView()
            }
            binding.root.onClickAlpha {
                if (isLocked) {
                    if ((layoutPosition - 1 == maxUnlockedEpisode)) {
                        Log.e("MOVIEEE", "show : unlockEpisodesDialog")
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