package com.shortdrama.movie.data.repository

import com.shortdrama.movie.data.dao.HistoryWatchDao
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow


@Singleton
class HistoryWatchRepository @Inject constructor(
    private val historyWatchDao: HistoryWatchDao
) {
    // Watch History
    suspend fun addHistoryWatchMovie(entity: HistoryWatchEntity) {
        historyWatchDao.addHistoryMovie(entity)
    }

    fun getAllHistoryWatchMovies(): Flow<List<HistoryWatchEntity>> {
        return historyWatchDao.getAllHistoryMovie()
    }

    suspend fun deleteHistoryWatchMovie(movieId: String) {
        historyWatchDao.deleteHistoryMovieById(movieId)
    }
}