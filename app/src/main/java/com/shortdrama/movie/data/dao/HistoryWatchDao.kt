package com.shortdrama.movie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryWatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistoryMovie(watchHistoryEntity: HistoryWatchEntity)

    @Query("SELECT * FROM ${AppConstants.TABLE_NAME_HISTORY_WATCH}")
    fun getAllHistoryMovie(): Flow<List<HistoryWatchEntity>>

    @Query("DELETE FROM ${AppConstants.TABLE_NAME_HISTORY_WATCH} WHERE drama_id = :movieId")
    suspend fun deleteHistoryMovieById(movieId: String)
}