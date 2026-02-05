package com.example.core_api.api

import com.example.core_api.model.core.SeriesEpisodeResponse
import com.example.core_api.model.core.SeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {

    //get all movie series by page
    @GET("api/series")
    suspend fun getTVSeries(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): SeriesResponse

    //get all movie series by category
    @GET("api/series")
    suspend fun getTVSeriesByGenreID(
        @Query("genreID") genreID: Int,
    ): SeriesResponse

    //get movie series episodes
    @GET("api/series/{id}/season/{seasonNumber}")
    suspend fun getTVSeriesEpisodes(
        @Path("id") id: Int,
        @Path("seasonNumber") seasonNumber: Int
    ): SeriesEpisodeResponse
}