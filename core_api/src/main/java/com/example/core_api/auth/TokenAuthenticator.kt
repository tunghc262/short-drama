package com.example.core_api.auth

import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator @Inject constructor(
    private val refresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.priorResponse != null) return null

        val newToken = runBlocking {
            refresher.refresh()
        } ?: return null

        return response.request.newBuilder()
            .header("x-token", newToken)
            .build()
    }
}
