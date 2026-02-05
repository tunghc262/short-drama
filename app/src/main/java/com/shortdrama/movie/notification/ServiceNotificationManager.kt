package com.shortdrama.movie.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.module.ads.utils.FBTracking
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.views.activities.notification.NotificationFullscreenActivity
import com.shortdrama.movie.views.activities.splash.SplashActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class ServiceNotificationManager @Inject constructor(
    @ApplicationContext val context: Context,
) {

    companion object {
        const val CHANNEL_ID = "daily_notification_channel"
        private const val CHANNEL_ID_LOCK = "daily_notification_channel_lock"
        const val REQUEST_CODE_APP_MAPS = 112
        const val REQUEST_CODE_FULL_INTENT = 113
        const val ID_NOTIFICATION_NORMAL = 1112
        const val ID_NOTIFICATION_LOCK = 1111
    }

    fun cancelAllNotification() {
        val notificationManager: NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun createNotificationNormalOffline(notificationData: NotificationData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val isChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (isChannel == null) {
                val nameJobHide = "Daily Notifications"
                val importanceJobHide = NotificationManager.IMPORTANCE_DEFAULT
                val channelJobHide = NotificationChannel(CHANNEL_ID, nameJobHide, importanceJobHide)
                notificationManager.createNotificationChannel(channelJobHide)
            }
        }
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ID_NOTIFICATION_NORMAL)
        val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, notificationData.res)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(bitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setBigContentTitle(notificationData.title)
                    .setSummaryText(notificationData.message)
                    .bigLargeIcon(null as Bitmap?)
            )
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getResultPendingIntent())
            .setAutoCancel(true)
            .setShowWhen(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        val notificationId = ID_NOTIFICATION_NORMAL
        Log.e("TAG", "createNotificationNormalOffline: ")
        FBTracking.funcTracking(context, "noti_statusbar_view", null)
        notificationManager.notify(notificationId, notification)
    }

    @SuppressLint("FullScreenIntentPolicy")
    fun createNotificationLock(notificationData: NotificationData) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = CHANNEL_ID_LOCK

        notificationManager.cancel(ID_NOTIFICATION_LOCK)
        val nameJobHide = "Daily Notifications Lock"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                nameJobHide,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        val fullScreenIntent = Intent(context, NotificationFullscreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(AppConstants.INTENT_NOTIFICATION_LOCK, notificationData)
        }

        fullScreenIntent.putExtra(AppConstants.INTENT_NOTIFICATION_LOCK, notificationData)

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_FULL_INTENT,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .build()
        FBTracking.funcTracking(context, "noti_lock_statusbar_view", null)
        notificationManager.notify(ID_NOTIFICATION_LOCK, notification)
    }

    private fun getResultPendingIntent(): PendingIntent {
        val resultIntent = Intent(context, SplashActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        resultIntent.putExtra(AppConstants.OPEN_FROM_NOTIFICATION, true)
        val resultPendingIntent = PendingIntent.getActivity(
            context, REQUEST_CODE_APP_MAPS, resultIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return resultPendingIntent
    }
}