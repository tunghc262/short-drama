package com.example.core_api.repository

import com.example.core_api.api.MoviesApiService
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class MoviesRepository @Inject constructor(
    private val apiService: MoviesApiService
) {
}