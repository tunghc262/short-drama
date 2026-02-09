package com.shortdrama.movie.data.models

import com.example.core_api.model.ui.TVSeriesUiModel


sealed class ForYouModel {
    data class Movie(
        val data: TVSeriesUiModel,
        val displayIndex: Int
    ) : ForYouModel()
    object Ads : ForYouModel()
}