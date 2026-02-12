package com.shortdrama.movie.views.dialogs

import android.content.Context
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogVideoLoadingBinding
import com.shortdrama.movie.views.bases.BaseDialog


class VideoLoadingDialog(context: Context) :
    BaseDialog<DialogVideoLoadingBinding>(context) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_video_loading
    }

    override fun initViews() {
        super.initViews()
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}