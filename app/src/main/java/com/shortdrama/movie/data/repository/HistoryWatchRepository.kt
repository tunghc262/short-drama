package com.shortdrama.movie.data.repository

import com.shortdrama.movie.data.dao.HistoryWatchDao
import jakarta.inject.Inject

class HistoryWatchRepository @Inject constructor(
    private val historyWatchDao: HistoryWatchDao
) {
}