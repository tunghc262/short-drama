package com.shortdrama.movie.data.repository

import com.shortdrama.movie.data.dao.MovieFavoriteDao
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class MovieFavoriteRepository @Inject constructor(
    private val movieFavoriteDao: MovieFavoriteDao
) {
    suspend fun addFavouriteMovie(entity: MovieFavoriteEntity) {
        movieFavoriteDao.addMovieFavorite(entity)
    }

    fun getAllFavouriteMovies(): Flow<List<MovieFavoriteEntity>> {
        return movieFavoriteDao.getAllMovieFavorites()
    }

    suspend fun deleteFavouriteMovie(movieId: String) {
        movieFavoriteDao.deleteMovieFavoriteById(movieId)
    }
}