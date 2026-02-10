package com.shortdrama.movie.utils

import android.content.Context
import com.module.ads.remote.FirebaseQuery
import java.util.Calendar

object UnlockEpisodeManager {
    private const val PREF_NAME = "reward_limit_prefs"
    private const val KEY_REWARD_COUNT = "reward_count"
    private const val KEY_LAST_RESET_DATE = "last_reset_date"

    fun canWatchReward(context: Context): Boolean {
        val today = getTodayMidnightMillis()
        val lastReset = SharePrefUtils.getLong(KEY_LAST_RESET_DATE, 0L)
        var count = SharePrefUtils.getInt(KEY_REWARD_COUNT, 0)
        if (lastReset < today) {
            SharePrefUtils.putLong(KEY_LAST_RESET_DATE, today)
            SharePrefUtils.putInt(KEY_REWARD_COUNT, 0)
            count = 0
        }
        return count < FirebaseQuery.getNumberLockMovie()
    }

    fun incrementRewardCount(context: Context) {
        val count = SharePrefUtils.getInt(KEY_REWARD_COUNT, 0) + 1
        SharePrefUtils.putInt(KEY_REWARD_COUNT, count)
    }

    fun getRemainingRewards(context: Context): Int {
        val today = getTodayMidnightMillis()
        val maxCount = FirebaseQuery.getNumberLockMovie().toInt()
        val lastReset = SharePrefUtils.getLong(KEY_LAST_RESET_DATE, 0L)
        if (lastReset < today) return maxCount
        return maxCount - SharePrefUtils.getInt(KEY_REWARD_COUNT, 0)
    }

    private fun getTodayMidnightMillis(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }
}