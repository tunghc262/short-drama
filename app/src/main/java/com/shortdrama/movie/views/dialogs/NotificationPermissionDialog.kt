package com.shortdrama.movie.views.dialogs

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogPermissionNotificationBinding
import com.shortdrama.movie.views.bases.BaseDialog

class NotificationPermissionDialog(context: Context) :
    BaseDialog<DialogPermissionNotificationBinding>(context) {

    override fun getLayoutDialog(): Int {
        return R.layout.dialog_permission_notification
    }

    override fun initViews() {
        super.initViews()
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvDenied.setOnClickListener {
            dismiss()
        }

        mBinding.tvAllow.setOnClickListener {
            val intent = Intent().apply {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> { // Android 8+
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }

                    else -> {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = "package:${context.packageName}".toUri()
                    }
                }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            dismiss()
        }
    }
}