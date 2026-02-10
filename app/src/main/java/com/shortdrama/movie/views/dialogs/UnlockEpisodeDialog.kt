package com.shortdrama.movie.views.dialogs

import android.app.Activity
import android.util.Log
import com.module.ads.admob.reward.RewardInApp
import com.module.ads.remote.FirebaseQuery
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogUnlockEpisodeBinding
import com.shortdrama.movie.utils.UnlockEpisodeManager
import com.shortdrama.movie.views.bases.BaseDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.showToastByString

class UnlockEpisodeDialog(
    val activity: Activity,
    val onClickUpgrade: () -> Unit,
    val onClickWatchAds: () -> Unit
) :
    BaseDialog<DialogUnlockEpisodeBinding>(activity) {
    override fun getLayoutDialog(): Int {
        return R.layout.dialog_unlock_episode
    }

    override fun initViews() {
        super.initViews()
        Log.e(
            "MOVIE",
            "UnlockEpisodeDialog free: ${UnlockEpisodeManager.getRemainingRewards(activity)}",
        )
        RewardInApp.getInstance().loadReward(activity)
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.btnUpgrade.onClickAlpha {
            dismiss()
            onClickUpgrade()
        }
        mBinding.btnWatchAds.onClickAlpha {
            if (UnlockEpisodeManager.canWatchReward(activity)) {
                dismiss()
                onClickWatchAds()
                UnlockEpisodeManager.incrementRewardCount(activity)
            } else {
                context.showToastByString("You've used up your ${FirebaseQuery.getNumberLockMovie()} ad views for today. Upgrade to Premium to continue watching!")
            }
        }
        mBinding.ivClose.onClickAlpha {
            dismiss()
        }
    }
}