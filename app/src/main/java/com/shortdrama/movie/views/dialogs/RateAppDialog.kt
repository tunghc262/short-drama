package com.shortdrama.movie.views.dialogs

import android.app.Activity
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogRateAppBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.views.bases.BaseDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class RateAppDialog(private val activity: Activity) : BaseDialog<DialogRateAppBinding>(activity) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_rate_app
    }

    override fun initViews() {
        super.initViews()
        mBinding.simpleRatingBar.rating = 5f

        mBinding.simpleRatingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                when (rating) {
                    1f -> {
                        mBinding.ivStart.setImageResource(R.drawable.img_star_1)
                    }

                    2f -> {
                        mBinding.ivStart.setImageResource(R.drawable.img_star_2)
                    }

                    3f -> {
                        mBinding.ivStart.setImageResource(R.drawable.img_star_3)
                    }

                    4f -> {
                        mBinding.ivStart.setImageResource(R.drawable.img_star_4)
                    }

                    5f -> {
                        mBinding.ivStart.setImageResource(R.drawable.img_star_5)
                    }
                }
            }
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvRateNow.onClickAlpha {
            dismiss()
            val star = mBinding.simpleRatingBar.rating
            if (star < 4) {
                val dialogFeedback = FeedbackDialog(context)
                dialogFeedback.show()
            } else {
                AppUtils.openMarket(context, context.packageName)
            }
        }

        mBinding.ivClose.onClickAlpha {
            dismiss()
        }
    }
}