package com.example.core_api.api

import com.example.core_api.auth.TokenRefresher
import com.example.core_api.auth.TokenStore
import com.example.core_api.utils.ApiConstants

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val store: TokenStore,
    private val refresher: TokenRefresher
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var token = store.getToken()
        if (token == null || store.isExpired()) {
            token = runBlocking {
                refresher.refresh()
            }
        }

        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("x-token", it)
            }
            addHeader("x-package-name", ApiConstants.PACKAGE_NAME)
        }.build()
        return chain.proceed(request)
    }
}

