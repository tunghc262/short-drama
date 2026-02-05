package com.shortdrama.movie.data.repository

import com.shortdrama.movie.data.dao.MovieFavoriteDao
import jakarta.inject.Inject

class MovieFavoriteRepository @Inject constructor(
    private val movieFavoriteDao: MovieFavoriteDao
) {

}