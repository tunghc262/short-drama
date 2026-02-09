package com.shortdrama.movie.views.dialogs

import android.content.Context
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogRemoveFavouriteBinding
import com.shortdrama.movie.views.bases.BaseDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class RemoveFavouriteDialog(context: Context, val onClickRemove: () -> Unit) :
    BaseDialog<DialogRemoveFavouriteBinding>(context) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_remove_favourite
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvRemove.onClickAlpha {
            dismiss()
            onClickRemove()
        }
        mBinding.tvCancel.onClickAlpha {
            dismiss()
        }
    }
}