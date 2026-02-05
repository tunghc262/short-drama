package com.example.core_api.model.core

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeModel(
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    @SerializedName("still_path")
    val stillPath: String?,
    val link: String
) : Parcelable