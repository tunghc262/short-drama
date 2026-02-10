package com.shortdrama.movie.views.activities.splash

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.module.ads.admob.aoa.OpenAdsManager
import com.module.ads.admob.banner.BannerSplash
import com.module.ads.admob.inters.IntersSplashAll
import com.module.ads.callback.CallbackBanner
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivitySplashBinding
import com.shortdrama.movie.notification.DailyWorkReceiver
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.utils.ConnectionLiveData
import com.shortdrama.movie.utils.PermissionUtils
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.utils.UsageManager
import com.shortdrama.movie.views.activities.language.LanguageActivity
import com.shortdrama.movie.views.activities.main.MainActivity
import com.shortdrama.movie.views.activities.premium.PremiumActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.isNetwork
import com.shortdrama.movie.views.bases.ext.onCheckActivityIsFinished
import com.shortdrama.movie.views.dialogs.NoInternetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var noInternetDialog: NoInternetDialog
    private var isNextActivity = AtomicBoolean(false)
    private var jobTimeout: Job? = null
    private var countDownTimer: CountDownTimer? = null

    private var bp: BillingProcessor? = null

    private var fromNotification = false
    private var fromNotificationFullScreen = false
    private var alarmManager: AlarmManager? = null

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                logEvent("popup_permission_noti_accept")
                if (FirebaseQuery.getEnableNotification()) {
                    scheduleDailyWork()
                }
            } else {
                logEvent("popup_permission_noti_deny")
            }
        }

    override fun getLayoutActivity(): Int {
        return R.layout.activity_splash
    }

    override fun onResume() {
        super.onResume()
        serviceNotificationManager?.cancelAllNotification()
        if (fromNotification) {
            if (!fromNotificationFullScreen) {
                logEvent("splash_noti_statusbar_view")
            } else {
                logEvent("splash_noti_lock_view")
            }
        }

        //connectionLiveData.updateConnection()
        if (FirebaseQuery.getIsShowOpenStart()) {
            OpenAdsManager.getOpenAds().showAdsOpenStart(this)
        } else {
            IntersSplashAll.getInstance().showAdsAll(this)
        }
    }

    override fun initViews() {
        startProgressBar()
        initPermission()
        initNotification()
        initIAP()
        initAds()

        if (AppUtils.isSession2()) {
            logEvent("splash_ss_view")
        } else {
            logEvent("splash_view")
        }
    }

    private fun startProgressBar() {
        val totalTime = 90000L // 30 giây
        val interval = 300L     // Cập nhật mỗi 300ms

        countDownTimer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                val progress =
                    ((totalTime - millisUntilFinished).toFloat() / totalTime * 100).toInt()
                mBinding.progressBar.progress = progress
            }

            override fun onFinish() {
                mBinding.progressBar.progress = 100
            }
        }

        countDownTimer?.start()

        jobTimeout = lifecycleScope.launch {
            delay(90000L)
            if (isActive) {
                nextActivity()
            }
        }
    }

    private fun initIAP() {
        bp = BillingProcessor.newBillingProcessor(
            this,
            PurchaseUtils.LICENSE_KEY,
            object : BillingProcessor.IBillingHandler {
                override fun onProductPurchased(
                    productId: String,
                    details: PurchaseInfo?
                ) {
                }

                override fun onPurchaseHistoryRestored() {}
                override fun onBillingError(errorCode: Int, error: Throwable?) {}

                override fun onBillingInitialized() {
                    // Đã kết nối thành công → Bắt đầu load purchases
                    loadUserSubscription()
                }

            })
        bp?.initialize() // Bắt đầu kết nối
    }

    private fun loadUserSubscription() {
        bp?.loadOwnedPurchasesFromGoogleAsync(object : BillingProcessor.IPurchasesResponseListener {
            override fun onPurchasesSuccess() {
                val hasSubscription = bp?.isSubscribed(PurchaseUtils.getIdWeek()) == true ||
                        bp?.isSubscribed(PurchaseUtils.getIdMonth()) == true

                if (hasSubscription) {
                    UsageManager.setAdsRemoved(this@SplashActivity, true)
                    UsageManager.resetForPurchase(this@SplashActivity)
                }
            }

            override fun onPurchasesError() {}
        })
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                logEvent("popup_permission_noti_view")
                requestNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initDialogInternet() {
        noInternetDialog = NoInternetDialog(this)
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { hasConnection ->
            if (hasConnection) {
                if (!onCheckActivityIsFinished() && noInternetDialog.isShowing) {
                    noInternetDialog.dismiss()
                    initAds()
                }
            } else {
                noInternetDialog.show()
            }
        }
        if (!isNetwork()) {
            if (noInternetDialog != null && !noInternetDialog.isShowing) {
                noInternetDialog.show()
            }
        }
    }

    private fun initAds() {
        val isChooseLanguage = SharePrefUtils.getBoolean(AppConstants.KEY_SELECT_LANGUAGE, false)
        FirebaseQuery.isChooseLanguage = isChooseLanguage
        FirebaseQuery.isFromNotification = fromNotification

        FirebaseQuery.getConfigController().initFirebase(this@SplashActivity) {
            nextActivity()
        }
        FirebaseQuery.getConfigController().setCallbackTimeout {
            jobTimeout?.cancel()
        }
        FirebaseQuery.getConfigController().setCallbackBannerSplash(object : CallbackBanner {
            override fun onLoaded() {
                BannerSplash.getInstance().showAds(mBinding.lnBanner)
            }

            override fun onFailed() {
                mBinding.lnBanner.goneView()
            }
        })
    }

    private fun nextActivity() {
        if (isNextActivity.getAndSet(true)) {
            return
        }
        if (fromNotification) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val isChooseLanguage =
                SharePrefUtils.getBoolean(AppConstants.KEY_SELECT_LANGUAGE, false)
            if (isChooseLanguage) {
                if (FirebaseQuery.getIsShowLanguageReopen()) {
                    val intent = Intent(this, LanguageActivity::class.java)
                    startActivity(intent)
                } else {
                    if (FirebaseQuery.getEnableIap() && PurchaseUtils.isNoAds(this) && FirebaseQuery.getEnableAds()) {
                        val intent = Intent(this, PremiumActivity::class.java)
                        intent.putExtra(AppConstants.IS_FROM_ONBOARDING, true)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                val intent = Intent(this, LanguageActivity::class.java)
                startActivity(intent)
            }
        }
        finish()
    }

    private fun initNotification() {
        intent?.let {
            if (it.hasExtra(AppConstants.OPEN_FROM_NOTIFICATION)) {
                fromNotification = it.getBooleanExtra(AppConstants.OPEN_FROM_NOTIFICATION, false)
            }
            if (it.hasExtra(AppConstants.OPEN_FROM_NOTIFICATION_FULL_SCREEN)) {
                fromNotification =
                    it.getBooleanExtra(AppConstants.OPEN_FROM_NOTIFICATION_FULL_SCREEN, false)
            }
        }


        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (fromNotification && !fromNotificationFullScreen) {
            logEvent("noti_statusbar_click")
        }

        if (!PermissionUtils.hasPostNotification(this)) {
            val isSession2 = SharePrefUtils.getBoolean(AppConstants.KEY_SESSION_2, false)
            if (!isSession2) {
                logEvent("popup_permission_noti_view")
                PermissionUtils.requestPostNotification(
                    this,
                    "You\'ve turned off notifications. Open Settings to turn important notifications back on."
                )
            }
        } else {
            if (FirebaseQuery.getEnableNotification()) {
                scheduleDailyWork()
            } else {
                cancelDailyWork()
            }
        }
    }

    private fun scheduleDailyWork() {
        cancelDailyWork()
        val pendingIntent = getDailyWorkPendingIntent()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 24)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelDailyWork() {
        val pendingIntent = getDailyWorkPendingIntent()
        alarmManager?.cancel(pendingIntent)
    }

    private fun getDailyWorkPendingIntent(): PendingIntent {
        val intent = Intent(this@SplashActivity, DailyWorkReceiver::class.java).apply {
            putExtra(
                AppConstants.ALARM_REQUEST_CODE,
                AppConstants.REQUEST_CODE_NOTIFICATION_NORMAL
            )
        }
        return PendingIntent.getBroadcast(
            this,
            AppConstants.REQUEST_CODE_NOTIFICATION_NORMAL,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onBackPressedCallback() {}

    override fun onDestroy() {
        bp?.release()
        countDownTimer?.cancel()
        jobTimeout?.cancel()
        //if (noInternetDialog != null && noInternetDialog.isShowing) {
        //    noInternetDialog.dismiss()
        //}
        super.onDestroy()
    }
}