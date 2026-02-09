package com.shortdrama.movie.utils

import android.content.Context
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.PlayMovieSpeedModel
import com.shortdrama.movie.data.models.ResolutionMovieModel
import com.shortdrama.movie.notification.NotificationData
import java.util.concurrent.ThreadLocalRandom

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

    val listPlaySpeedMovie = listOf(
        PlayMovieSpeedModel(0, "0.5x", 0.5f),
        PlayMovieSpeedModel(1, "0.75x", 0.75f),
        PlayMovieSpeedModel(2, "1x", 1f),
        PlayMovieSpeedModel(3, "1.25x", 1.25f),
        PlayMovieSpeedModel(4, "1.5x", 1.5f),
        PlayMovieSpeedModel(5, "1.75x", 1.75f),
        PlayMovieSpeedModel(6, "2x", 2f),
        PlayMovieSpeedModel(7, "3x", 3f)
    )

    val listResolution = listOf(
        ResolutionMovieModel(0, "Auto (Recommend)", "Adjust to optimize your experience"),
        ResolutionMovieModel(1, "1080P", "High resolution"),
        ResolutionMovieModel(2, "720P", "Medium resolution"),
        ResolutionMovieModel(3, "480P", "Low resolution"),
    )

    fun randomFutureDate(): Long {
        val now = System.currentTimeMillis() + (24L * 60 * 60 * 1000)
        val twoWeeksLater = now + (14L * 24 * 60 * 60 * 1000)
        return ThreadLocalRandom.current().nextLong(now, twoWeeksLater + 1)
    }

    fun generateComingSoonDates(count: Int): List<Long> {
        val now = System.currentTimeMillis() + (24L * 60 * 60 * 1000)
        val daysInMillis = (0 until 14).map { now + it * 24L * 60 * 60 * 1000 }
        val shuffledDays = daysInMillis.shuffled()
        return shuffledDays.take(count.coerceAtMost(shuffledDays.size))
    }
}