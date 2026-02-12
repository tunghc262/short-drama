package com.shortdrama.movie.app

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.os.Build
import android.os.StrictMode
import android.webkit.WebView
import com.google.android.gms.ads.AdActivity
import com.module.ads.app.ModuleApplication
import com.shortdrama.movie.BuildConfig
import com.shortdrama.movie.notification.ServiceNotificationManager
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.activities.language.LanguageActivity
import com.shortdrama.movie.views.activities.language.LanguageDupActivity
import com.shortdrama.movie.views.activities.notification.NotificationFullscreenActivity
import com.shortdrama.movie.views.activities.onboard.OnboardActivity
import com.shortdrama.movie.views.activities.premium.PremiumActivity
import com.shortdrama.movie.views.activities.splash.SplashActivity
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GlobalApp : ModuleApplication() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: GlobalApp

        var isShowDialogFullIntent = false
        var isShowDialogNotification = false

        var isShowRecommendDialog = false

    }

    init {
        instance = this
    }

    @Inject
    lateinit var serviceNotificationManager: ServiceNotificationManager

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    //.penaltyDeath() // crash app.
                    .build()
            )
        }

        SharePrefUtils.init(this)

        addExcludedActivity(SplashActivity::class.java)
        addExcludedActivity(LanguageActivity::class.java)
        addExcludedActivity(LanguageDupActivity::class.java)
        addExcludedActivity(OnboardActivity::class.java)
        addExcludedActivity(AdActivity::class.java)
        addExcludedActivity(PremiumActivity::class.java)
        addExcludedActivity(NotificationFullscreenActivity::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName()
            if (processName !== packageName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }

    fun isAppInForeground(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = am.runningAppProcesses ?: return false

        for (process in appProcesses) {
            if (process.processName == packageName) {
                return process.importance ==
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}