package com.example.core_api.model.core

import com.example.core_api.model.ui.VideoListTVSeriesModel
import com.google.gson.annotations.SerializedName

data class TVSeriesModel(
    val id: Int,

    @SerializedName("tmdb_id")
    val tmdbId: Int,

    @SerializedName("imdb_id")
    val imdbId: String?,

    val name: String,

    @SerializedName("original_name")
    val originalName: String?,

    val overview: String?,

    val tagline: String?,

    @SerializedName("first_air_date")
    val firstAirDate: String?,

    @SerializedName("last_air_date")
    val lastAirDate: String?,

    val status: String?,

    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int?,

    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("vote_average")
    val voteAverage: String?,

    @SerializedName("vote_count")
    val voteCount: Int?,

    val popularity: String?,

    @SerializedName("original_language")
    val originalLanguage: String?,

    val type: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    val homepage: String?,

    @SerializedName("in_production")
    val inProduction: Int?,

    val languages: List<String>?,

    val networks: List<Network>?,

    @SerializedName("created_by")
    val createdBy: List<Creator>?,

    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>?,

    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>?,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>?,

    @SerializedName("status_tag")
    val statusTag: String?,

    val keywords: List<Keyword>?,

    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: Episode?,

    @SerializedName("next_episode_to_air")
    val nextEpisodeToAir: Episode?,
    val genres: List<GenreTVSeriesModel>?,
    val videos: List<VideoListTVSeriesModel>?
)
