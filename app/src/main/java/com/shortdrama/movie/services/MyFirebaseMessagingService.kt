package com.shortdrama.movie.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.views.activities.splash.SplashActivity
import java.io.IOException

internal const val REQUEST_NOTIFICATION = 1
internal const val NOTIFICATION_ID = 96

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = remoteMessage.notification
        if (notification != null) {
            val title = notification.title
            val message = notification.body
            val imageUrl = notification.imageUrl
            sendNotification(title, message, imageUrl)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("TAG", "onNewToken: $token")
    }

    private fun sendNotification(title: String?, message: String?, uriImage: Uri?) {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra(AppConstants.OPEN_APP_FROM_NOTIFICATION, true)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                REQUEST_NOTIFICATION,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                this,
                REQUEST_NOTIFICATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val builder =
            NotificationCompat.Builder(this, resources.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setDefaults(Notification.DEFAULT_VIBRATE and Notification.DEFAULT_SOUND)
                .setLights(Color.BLUE, 5000, 5000)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentTitle(title ?: "")
                .setContentText(message ?: "")
                .setContentIntent(pendingIntent)
        Log.e("TAG", "sendNotification: ${uriImage.toString()}")
        if (uriImage != null && uriImage.toString() != "") {
            try {
                Glide.with(this).asBitmap().load(uriImage)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "sendNotification: glide ${e?.message}")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            model: Any,
                            target: Target<Bitmap>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "sendNotification: glide onResourceReady: $resource")
                            builder.setStyle(
                                NotificationCompat.BigPictureStyle().bigPicture(resource)
                                    .setSummaryText(message)
                            )
                            val notificationManager =
                                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            createNotificationChannel(
                                this@MyFirebaseMessagingService,
                                notificationManager
                            )
                            notificationManager.notify(NOTIFICATION_ID, builder.build())
                            return true
                        }
                    }).submit()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(
                this@MyFirebaseMessagingService,
                notificationManager
            )
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(
        context: Context,
        notificationManager: NotificationManager
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val descriptionText = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                context.resources.getString(R.string.notification_channel_id),
                name,
                importance
            ).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}