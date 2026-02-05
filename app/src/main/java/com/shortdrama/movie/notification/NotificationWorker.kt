package com.shortdrama.movie.notification

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.module.ads.remote.FirebaseQuery
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.app.GlobalApp
import com.shortdrama.movie.utils.DataUtils
import com.shortdrama.movie.utils.PermissionUtils
import java.util.Locale

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val requestCode = inputData.getInt(AppConstants.ALARM_REQUEST_CODE, -1)
        if (isSendNotification() && !GlobalApp.instance.isAppInForeground()) {
            checkAppStateAndNotify(requestCode)
        }
        Log.e("TAG", "doWork: ${Result.failure()}")
        return Result.success()
    }

    private fun checkAppStateAndNotify(requestCode: Int) {
        val serviceNotificationManager = (context as GlobalApp).serviceNotificationManager
        val notification: NotificationData? = when (requestCode) {
            AppConstants.REQUEST_CODE_NOTIFICATION_NORMAL -> getNotificationNormalRandom()
            AppConstants.REQUEST_CODE_NOTIFICATION_LOCK -> getNotificationLockRandom()
            else -> {
                Log.e("TAG", "checkAppStateAndNotify : request code : $requestCode ")
                getNotificationNormalRandom()
            }
        }
        if (requestCode == AppConstants.REQUEST_CODE_NOTIFICATION_NORMAL) {
            if (FirebaseQuery.getEnableNotification()) {
                notification?.let {
                    serviceNotificationManager.createNotificationNormalOffline(it)
                }
            } else {
                Log.e("TAG", ":checkAppStateAndNotify getNotificationScr false")
            }
        }
        if (requestCode == AppConstants.REQUEST_CODE_NOTIFICATION_LOCK) {
            if (FirebaseQuery.getEnableNotificationLockScr()) {
                notification?.let {
                    serviceNotificationManager.createNotificationLock(it)
                }
            } else {
                Log.e("TAG", ":checkAppStateAndNotify getNotificationLockScr false")
            }
        }
    }

    private fun getNotificationLockRandom(): NotificationData? {
        val locale = Locale.getDefault().language
        return if (locale in arrayListOf("US", "GB", "BR")) {
            DataUtils.getNotificationLockCountry(context).randomOrNull()
        } else {
            DataUtils.getNotificationLock(context).randomOrNull()
        }
    }

    private fun getNotificationNormalRandom(): NotificationData? {
        val locale = Locale.getDefault().language
        return if (locale in arrayListOf("US", "GB", "BR")) {
            DataUtils.getNotificationNormalCountry(context).randomOrNull()
        } else {
            DataUtils.getNotificationNormal(context).randomOrNull()
        }
    }

    private fun isSendNotification(): Boolean {
        return PermissionUtils.hasPostNotification(context)
    }

    fun getUserCountry(context: Context): String {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.simCountryIso?.let {
            if (it.isNotEmpty()) return it.uppercase()
        }
        telephony.networkCountryIso?.let {
            if (it.isNotEmpty()) return it.uppercase()
        }
        return Locale.getDefault().country.uppercase()
    }

}