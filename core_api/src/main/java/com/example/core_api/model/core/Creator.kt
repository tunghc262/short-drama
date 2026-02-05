package com.example.core_api.model.core

import com.google.gson.annotations.SerializedName

data class Creator(
    val id: Int,
    val name: String,
    @SerializedName("original_name")
    val originalName: String?,
    val gender: Int?,
    @SerializedName("profile_path")
    val profilePath: String?
)