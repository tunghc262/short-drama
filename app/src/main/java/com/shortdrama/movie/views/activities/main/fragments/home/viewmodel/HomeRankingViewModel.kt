package com.shortdrama.movie.views.activities.main.fragments.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_api.model.ui.TVSeriesUiModel
import com.example.core_api.repository.MoviesRepository
import com.shortdrama.movie.utils.DataUtils
import com.shortdrama.movie.views.bases.ext.toMonthDayOrdinal
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeRankingViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val _ranking = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val ranking = _ranking.asStateFlow()

    fun loadRanking(genresId: Int) {
        viewModelScope.launch {
            _ranking.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }
}