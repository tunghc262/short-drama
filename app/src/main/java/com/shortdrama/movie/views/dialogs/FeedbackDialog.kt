package com.shortdrama.movie.views.dialogs

import android.content.Context
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.DialogFeedbackBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.views.bases.BaseDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.showToastByString

class FeedbackDialog(context: Context) : BaseDialog<DialogFeedbackBinding>(context) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_feedback
    }

    override fun initViews() {
        super.initViews()
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivClose.onClickAlpha {
            dismiss()
        }
        mBinding.btnFeedback.onClickAlpha {
            val feedback = mBinding.edtFeedback.text.trim()
            if (feedback == "") {
                context.showToastByString("Please enter your feedback!")
            } else {
                dismiss()
                AppUtils.sendEmailMore(
                    context,
                    arrayOf(AppConstants.EMAIL_FEEDBACK),
                    "",
                    feedback.toString()
                )
            }
        }
    }
}