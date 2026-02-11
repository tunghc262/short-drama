package com.shortdrama.movie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovieFavorite(favouriteMovieEntity: MovieFavoriteEntity)

    @Query("SELECT * FROM ${AppConstants.TABLE_NAME_MOVIE_FAVORITE}")
    fun getAllMovieFavorites(): Flow<List<MovieFavoriteEntity>>

    @Query("DELETE FROM ${AppConstants.TABLE_NAME_MOVIE_FAVORITE} WHERE drama_id = :dramaId")
    suspend fun deleteMovieFavoriteById(dramaId: String)
}