package com.example.core_api.model.core

import com.google.gson.annotations.SerializedName

data class TVSeriesEpisodeModel(
    val id: Int,

    @SerializedName("tv_series_id")
    val tvSeriesId: Int,

    @SerializedName("season_number")
    val seasonNumber: String?,

    @SerializedName("air_date")
    val airDate: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    val episodes: List<EpisodeModel>?
)
