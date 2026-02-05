package com.shortdrama.movie.utils

import android.content.Context
import com.shortdrama.movie.R
import com.shortdrama.movie.notification.NotificationData

object DataUtils {
    fun getNotificationLockCountry(context: Context): List<NotificationData> {
        return listOf(
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_1_country),
                message = context.resources.getString(R.string.notification_lock_scr_content_1_country),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_2_country),
                message = context.resources.getString(R.string.notification_lock_scr_content_2_country),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_3_country),
                message = context.resources.getString(R.string.notification_lock_scr_content_3_country),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_4_country),
                message = context.resources.getString(R.string.notification_lock_scr_content_4_country),
                res = R.drawable.img_on_board_1
            )
        )
    }

    fun getNotificationLock(context: Context): List<NotificationData> {
        return listOf(
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_1),
                message = context.resources.getString(R.string.notification_lock_scr_content_1),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_2),
                message = context.resources.getString(R.string.notification_lock_scr_content_2),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_3),
                message = context.resources.getString(R.string.notification_lock_scr_content_3),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_lock_scr_title_4),
                message = context.resources.getString(R.string.notification_lock_scr_content_4),
                res = R.drawable.img_on_board_1
            )
        )
    }

    fun getNotificationNormalCountry(context: Context): List<NotificationData> {
        return listOf(
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_1_country),
                message = context.resources.getString(R.string.notification_scr_content_1_country),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_2_country),
                message = context.resources.getString(R.string.notification_scr_content_2_country),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_3_country),
                message = context.resources.getString(R.string.notification_scr_content_3_country),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_4_country),
                message = context.resources.getString(R.string.notification_scr_content_4_country),
                res = R.drawable.img_on_board_1
            )
        )
    }

    fun getNotificationNormal(context: Context): List<NotificationData> {
        return listOf(
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_1),
                message = context.resources.getString(R.string.notification_scr_content_1),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_2),
                message = context.resources.getString(R.string.notification_scr_content_2),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_3),
                message = context.resources.getString(R.string.notification_scr_content_3),
                res = R.drawable.img_on_board_1
            ),
            NotificationData(
                title = context.resources.getString(R.string.notification_scr_title_4),
                message = context.resources.getString(R.string.notification_scr_content_4),
                res = R.drawable.img_on_board_1
            )
        )
    }
}