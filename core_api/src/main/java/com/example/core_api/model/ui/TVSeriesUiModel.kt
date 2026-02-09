package com.example.core_api.model.ui

import android.os.Parcelable
import com.example.core_api.model.core.GenreTVSeriesModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TVSeriesUiModel(
    val id: Int = 0,

    val tmdbId: Int = 0,

    val imdbId: String? = null,

    val name: String? = null,

    val originalName: String? = null,

    val overview: String? = null,

    val tagline: String? = null,

    val firstAirDate: String? = null,

    val lastAirDate: String? = null,

    val status: String? = null,

    val numberOfSeasons: Int? = 0,

    val numberOfEpisodes: Int? = 0,

    val posterPath: String? = null,

    val backdropPath: String? = null,

    val voteAverage: String? = null,

    val voteCount: Int? = 0,

    val popularity: String? = null,

    val originalLanguage: String? = null,

    val type: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val homepage: String? = null,

    val inProduction: Int? = 0,

    val languages: List<String>? = emptyList(),

    val statusTag: String? = null,

    val genres: List<GenreTVSeriesModel>? = emptyList(),

    val videos: List<VideoListTVSeriesModel>? = emptyList(),

    val dateComingSoon: Long? = 0L

) : Parcelable