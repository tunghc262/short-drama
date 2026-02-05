package com.shortdrama.movie.views.dialogs

import android.content.Context
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogExitAppBinding
import com.shortdrama.movie.views.bases.BaseDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class ExitAppDialog(context: Context, val onClickExit: () -> Unit) :
    BaseDialog<DialogExitAppBinding>(context) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_exit_app
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvExit.onClickAlpha {
            dismiss()
            onClickExit()
        }
        mBinding.tvContinue.onClickAlpha {
            dismiss()
        }
    }
}