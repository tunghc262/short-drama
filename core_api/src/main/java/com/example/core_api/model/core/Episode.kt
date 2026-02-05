package com.example.core_api.model.core

import com.google.gson.annotations.SerializedName

data class Episode(
    val id: Int,
    val name: String,
    val overview: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    @SerializedName("episode_type")
    val episodeType: String?,
    val runtime: Int?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("show_id")
    val showId: Int?,
    @SerializedName("still_path")
    val stillPath: String?
)