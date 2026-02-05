package com.shortdrama.movie.utils

import android.content.Context
import com.anjlab.android.iab.v3.BillingProcessor
import com.module.ads.utils.PurchaseUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UsageManager {
    private const val PREF_NAME = "usage_prefs"
    private const val KEY_LAST_DATE = "last_date"
    private const val KEY_GEN_COUNT = "gen_count"  // lượt đã dùng
    private const val KEY_PLAN = "active_plan"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Lấy gói hiện tại
    fun getActivePlan(context: Context, bp: BillingProcessor?): String? {
        //return getPrefs(context).getString(KEY_PLAN, null)
        return when {
            bp?.isSubscribed("sub_month") == true -> "sub_month"
            bp?.isSubscribed("sub_week") == true -> "sub_week"
            else -> null
        }
    }

    fun setActivePlan(context: Context, activePlan: String) {
        getPrefs(context).edit().putString(KEY_PLAN, activePlan).apply()
    }

    private fun getMaxGen(plan: String?): Int = when (plan) {
        "sub_week" -> 30
        "sub_month" -> 50
        else -> 3
    }

    // Kiểm tra có thể gen không
    fun canGenerate(context: Context, bp: BillingProcessor?): Boolean {
        val prefs = getPrefs(context)
        val today = getTodayDate()
        val lastDate = prefs.getString(KEY_LAST_DATE, null)
        val activePlan = getActivePlan(context, bp)
        val cachedPlan = prefs.getString(KEY_PLAN, null) ?: activePlan
        val max = getMaxGen(cachedPlan)

        // Cập nhật plan
        if (activePlan != null) {
            prefs.edit().putString(KEY_PLAN, activePlan).apply()
        }

        // Ngày mới → reset
        if (lastDate != today) {
            prefs.edit()
                .putString(KEY_LAST_DATE, today)
                .putInt(KEY_GEN_COUNT, 0)
                .apply()
            return true
        }

        val used = prefs.getInt(KEY_GEN_COUNT, 0)
        return used < max
    }

    // Tăng lượt gen
    fun incrementGenCount(context: Context) {
        val prefs = getPrefs(context)
        val today = getTodayDate()
        val current = prefs.getInt(KEY_GEN_COUNT, 0)
        prefs.edit()
            .putString(KEY_LAST_DATE, today)
            .putInt(KEY_GEN_COUNT, current + 1)
            .apply()
    }

    // Lấy lượt còn lại
    fun getRemainingGen(context: Context, bp: BillingProcessor?): Int {
        val prefs = getPrefs(context)
        val today = getTodayDate()
        val lastDate = prefs.getString(KEY_LAST_DATE, null)
        val plan = prefs.getString(KEY_PLAN, null) ?: getActivePlan(context, bp)
        val max = getMaxGen(plan)

        return if (lastDate == today) {
            val used = prefs.getInt(KEY_GEN_COUNT, 0)
            (max - used).coerceAtLeast(0)
        } else {
            max
        }
    }

    // Xóa ads
    fun isAdsRemoved(context: Context) =
        PurchaseUtils.isNoAds(context)

    fun setAdsRemoved(context: Context, removed: Boolean = true) {
        PurchaseUtils.setNoAds(context, removed)
    }

    // Reset khi mua sub
    fun resetForPurchase(context: Context) {
        val prefs = getPrefs(context)
        val today = getTodayDate()
        PurchaseUtils.setNoAds(context, true)
        prefs.edit()
            .putString(KEY_LAST_DATE, today)
            .putInt(KEY_GEN_COUNT, 0)
            .apply()
    }

    private fun getTodayDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
}