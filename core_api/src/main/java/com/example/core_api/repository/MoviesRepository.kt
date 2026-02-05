package com.example.core_api.repository

import android.util.Log
import com.example.core_api.api.MoviesApiService
import com.example.core_api.mapper.toTVSeriesEpisodeUiModel
import com.example.core_api.mapper.toTVSeriesUiModel
import com.example.core_api.model.ui.TVSeriesEpisodeUiModel
import com.example.core_api.model.ui.TVSeriesUiModel
import jakarta.inject.Inject
import jakarta.inject.Singleton
import retrofit2.HttpException
import java.io.IOException


@Singleton
class MoviesRepository @Inject constructor(
    private val apiService: MoviesApiService
) {

    suspend fun getTVSeries(page: Int): List<TVSeriesUiModel> {
        try {
            return apiService.getTVSeries(
                limit = 20,
                page = page
            ).data.map { it.toTVSeriesUiModel() }
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.e("API", "Unauthorized - token expired or invalid")
            }
            return emptyList()

        } catch (e: IOException) {
            Log.e("API", "Network error", e)
            return emptyList()

        } catch (e: Exception) {
            Log.e("API", "Unknown error", e)
            return emptyList()
        }
    }

    suspend fun getTVSeriesByGenreID(genreID: Int): List<TVSeriesUiModel> {
        return try {
            apiService.getTVSeriesByGenreID(genreID).data
                .map { it.toTVSeriesUiModel() }
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.e("API", "Unauthorized - token expired or invalid")
            }
            emptyList()

        } catch (e: IOException) {
            Log.e("API", "Network error", e)
            emptyList()

        } catch (e: Exception) {
            Log.e("API", "Unknown error", e)
            emptyList()
        }
    }

    suspend fun getTVSeriesEpisodes(
        id: Int,
        numberSeasons: Int
    ): TVSeriesEpisodeUiModel? {
        return try {
            val response = apiService.getTVSeriesEpisodes(id, numberSeasons)
            response.data.toTVSeriesEpisodeUiModel()
        } catch (e: Exception) {
            null
        }
    }
}