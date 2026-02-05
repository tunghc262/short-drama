package com.example.core_api.mapper

import com.example.core_api.model.core.TVSeriesEpisodeModel
import com.example.core_api.model.core.TVSeriesModel
import com.example.core_api.model.ui.TVSeriesEpisodeUiModel
import com.example.core_api.model.ui.TVSeriesUiModel

fun TVSeriesModel.toTVSeriesUiModel(): TVSeriesUiModel {
    return TVSeriesUiModel(
        id = id,
        tmdbId = tmdbId,
        imdbId = imdbId,
        name = name,
        originalName = originalName,
        overview = overview,
        tagline = tagline,
        firstAirDate = firstAirDate,
        lastAirDate = lastAirDate,
        status = status,
        numberOfSeasons = numberOfSeasons,
        numberOfEpisodes = numberOfEpisodes,
        posterPath = posterPath,
        backdropPath = backdropPath,

        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,

        originalLanguage = originalLanguage,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt,
        homepage = homepage,
        inProduction = inProduction,
        languages = languages,
        statusTag = statusTag,
        genres = genres,
        videos = videos
    )
}

fun TVSeriesEpisodeModel.toTVSeriesEpisodeUiModel(): TVSeriesEpisodeUiModel {
    return TVSeriesEpisodeUiModel(
        id = id,
        tvSeriesId = tvSeriesId,
        seasonNumber = seasonNumber,
        airDate = airDate,
        posterPath = posterPath,
        episodes = episodes
    )
}