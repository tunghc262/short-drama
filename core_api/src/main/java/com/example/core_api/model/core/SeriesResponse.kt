package com.example.core_api.model.core

data class SeriesResponse(
    val status: String,
    val data: List<TVSeriesModel>,
    val pagination: Pagination
)