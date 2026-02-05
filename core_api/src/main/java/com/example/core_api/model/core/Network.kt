package com.example.core_api.model.core

import com.google.gson.annotations.SerializedName

data class Network(
    val id: Int,
    val name: String,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("origin_country")
    val originCountry: String?
)