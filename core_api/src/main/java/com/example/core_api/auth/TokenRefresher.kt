package com.example.core_api.auth

import com.module.ads.remote.FirebaseQuery

class TokenRefresher(
    private val api: AuthenApi,
    private val store: TokenStore
) {

    suspend fun refresh(): String? {
        val res = api.refreshToken(FirebaseQuery.getKeyApiToken())
        if (res.status != "success") return null

        store.save(
            token = res.access_token,
            expiredAt = res.expires_in
        )
        return res.access_token
    }
}
