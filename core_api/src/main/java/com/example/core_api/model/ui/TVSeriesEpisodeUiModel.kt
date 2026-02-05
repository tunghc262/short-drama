package com.example.core_api.model.ui

import android.os.Parcelable
import com.example.core_api.model.core.EpisodeModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVSeriesEpisodeUiModel(
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
) : Parcelable
