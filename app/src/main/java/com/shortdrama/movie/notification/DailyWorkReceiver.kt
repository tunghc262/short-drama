package com.shortdrama.movie.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shortdrama.movie.app.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DailyWorkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val requestCode = intent.getIntExtra(AppConstants.ALARM_REQUEST_CODE, -1)

        val workData = workDataOf(AppConstants.ALARM_REQUEST_CODE to requestCode)
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(workData)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)

        scheduleNextAlarm(context, requestCode)
    }

    fun scheduleNextAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyWorkReceiver::class.java).apply {
            putExtra(
                AppConstants.ALARM_REQUEST_CODE,
                requestCode
            )
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(
                Calendar.HOUR_OF_DAY,
                if (requestCode == AppConstants.REQUEST_CODE_NOTIFICATION_NORMAL) 19 else 9
            )
            set(
                Calendar.MINUTE,
                if (requestCode == AppConstants.REQUEST_CODE_NOTIFICATION_NORMAL) 24 else 59
            )
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }
}