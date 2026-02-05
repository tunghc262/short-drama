package com.shortdrama.movie.views.activities.notification

import android.content.Intent
import android.os.Build
import com.module.ads.utils.FBTracking
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivityNotificationFullscreenBinding
import com.shortdrama.movie.notification.NotificationData
import com.shortdrama.movie.views.activities.splash.SplashActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.onClick
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NotificationFullscreenActivity : BaseActivity<ActivityNotificationFullscreenBinding>() {

    private var notification: NotificationData? = null

    override fun getLayoutActivity(): Int {
        return R.layout.activity_notification_fullscreen
    }

    override fun initViews() {
        super.initViews()
        intent?.let {
            notification = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableExtra(
                    AppConstants.INTENT_NOTIFICATION_LOCK,
                    NotificationData::class.java
                )
            } else {
                it.getParcelableExtra(AppConstants.INTENT_NOTIFICATION_LOCK)
            })
        }

        val cal: Calendar = Calendar.getInstance()
        val currentHour: Int = cal.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = cal.get(Calendar.MINUTE)
        val formattedHour = String.format(Locale.ROOT, "%02d", currentHour)
        val formattedMinute = String.format(Locale.ROOT, "%02d", currentMinute)
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.ENGLISH)
        val date = dateFormat.format(Date())
        mBinding.tvTitle.text = notification?.title
        mBinding.tvContent.text = notification?.message
        mBinding.ivThumb.setImageResource(notification?.res ?: 0)
        mBinding.tvDate.text = date
        mBinding.tvHour.text = formattedHour
        mBinding.tvMinute.text = formattedMinute

        FBTracking.funcTracking(this, "noti_lock_statusbar_click", null)
        FBTracking.funcTracking(this, "noti_lock_view", null)
    }


    override fun onClickViews() {
        mBinding.icClose.onClick {
            finish()
        }

        mBinding.tvEnhance.onClick {
            FBTracking.funcTracking(this, "noti_lock_click", null)
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(AppConstants.OPEN_FROM_NOTIFICATION, true)
            intent.putExtra(AppConstants.OPEN_FROM_NOTIFICATION_FULL_SCREEN, true)
            startActivity(intent)
            finish()
        }
    }
}