package com.example.core_api.model.core

data class TokenResponse(
    val status: String,
    val expires_in: Long,
    val access_token: String
)