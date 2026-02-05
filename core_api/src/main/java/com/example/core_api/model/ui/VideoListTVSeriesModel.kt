package com.example.core_api.model.ui

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoListTVSeriesModel(
    val id: Int,
    @SerializedName("tv_series_id")
    val tvSeriesId: Int,
    val name: String,
    val link: String,
    val type: String,
    val official: Int
) : Parcelable