package com.shortdrama.movie.views.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogNoInternetBinding
import com.shortdrama.movie.views.bases.BaseDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class NoInternetDialog(context: Context) : BaseDialog<DialogNoInternetBinding>(context) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_no_internet
    }

    override fun initViews() {
        super.initViews()
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvRetry.onClickAlpha {
            dismiss()
            try {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}