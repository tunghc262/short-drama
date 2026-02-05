package com.example.core_api.auth

import android.content.Context

class TokenStore(context: Context) {

    private val prefs =
        context.getSharedPreferences("core_api_token", Context.MODE_PRIVATE)

    fun getToken(): String? =
        prefs.getString("token", null)

    fun save(token: String, expiredAt: Long) {
        prefs.edit()
            .putString("token", token)
            .putLong("expired_at", expiredAt)
            .apply()
    }

    fun isExpired(): Boolean {
        val expiredAt = prefs.getLong("expired_at", 0L)
        return System.currentTimeMillis() >= expiredAt
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
