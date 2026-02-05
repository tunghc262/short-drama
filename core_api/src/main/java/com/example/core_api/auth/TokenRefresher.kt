package com.example.core_api.auth

class TokenRefresher(
    private val api: AuthenApi,
    private val store: TokenStore
) {
    //FirebaseQuery.getKeyApiToken()
    suspend fun refresh(): String? {
        val res = api.refreshToken("ffb24ad430fe9dd87c5d54547e57354d")
        if (res.status != "success") return null

        store.save(
            token = res.access_token,
            expiredAt = res.expires_in
        )
        return res.access_token
    }
}
