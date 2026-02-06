package com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import com.shortdrama.movie.data.repository.HistoryWatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyWatchRepository: HistoryWatchRepository
) : ViewModel() {
    private val _watchHistory = MutableStateFlow<List<HistoryWatchEntity>>(emptyList())
    val watchHistory: StateFlow<List<HistoryWatchEntity>> = _watchHistory.asStateFlow()

    init {
        viewModelScope.launch {
            historyWatchRepository.getAllHistoryWatchMovies().collect { list ->
                _watchHistory.value = list
            }
        }
    }

    fun addToWatchHistory(movie: HistoryWatchEntity) {
        viewModelScope.launch {
            historyWatchRepository.addHistoryWatchMovie(movie)
        }
    }
}