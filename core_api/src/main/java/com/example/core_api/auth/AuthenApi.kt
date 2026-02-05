package com.example.core_api.auth

import com.example.core_api.model.core.TokenResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthenApi {
    @GET("api/token/refresh")
    suspend fun refreshToken(
        @Header("x-apikey") apiKey: String
    ): TokenResponse
}
