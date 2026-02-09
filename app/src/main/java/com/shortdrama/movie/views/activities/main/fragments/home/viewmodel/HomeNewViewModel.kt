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
class HomeNewViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val _exclusive = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val exclusive = _exclusive.asStateFlow()
    private val _newRelease = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val newRelease = _newRelease.asStateFlow()

    private val _comingSoon = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val comingSoon = _comingSoon.asStateFlow()

    fun loadNewRelease(genresId: Int) {
        viewModelScope.launch {
            _newRelease.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }

    fun loadExclusive(genresId: Int) {
        viewModelScope.launch {
            _exclusive.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }

    fun loadComingSoon(genresId: Int) {
        viewModelScope.launch {
            val originalList = moviesRepository.getTVSeriesByGenreID(genresId).take(10)
            val comingSoonDates = DataUtils.generateComingSoonDates(10)
            val listComingSoon = originalList.mapIndexed { index, item ->
                item.copy(dateComingSoon = comingSoonDates[index])
            }.sortedBy { it.dateComingSoon }
            _comingSoon.value = listComingSoon
        }
    }
}