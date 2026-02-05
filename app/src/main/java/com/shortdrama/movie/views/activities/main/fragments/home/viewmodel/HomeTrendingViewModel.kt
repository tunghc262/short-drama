package com.shortdrama.movie.views.activities.main.fragments.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_api.model.ui.TVSeriesUiModel
import com.example.core_api.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeTrendingViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    init {
        Log.e("TAG", "init HomeTrendingViewModel")
    }

    //banner home popular
    private val _banner = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val banner: StateFlow<List<TVSeriesUiModel>> = _banner.asStateFlow()

    //popular
    private val _popular = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val popular: StateFlow<List<TVSeriesUiModel>> = _popular.asStateFlow()

    //new release
    private val _newRelease = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val newRelease: StateFlow<List<TVSeriesUiModel>> = _newRelease.asStateFlow()

    //new release
    private val _topChart = MutableStateFlow<List<TVSeriesUiModel>>(emptyList())
    val topChart: StateFlow<List<TVSeriesUiModel>> = _topChart.asStateFlow()


    fun loadMovieBanner(genresId: Int) {
        viewModelScope.launch {
            _banner.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }

    fun loadMoviePopular(genresId: Int) {
        viewModelScope.launch {
            _popular.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }

    fun loadMovieNewRelease(genresId: Int) {
        viewModelScope.launch {
            _newRelease.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }

    fun loadMovieTopChart(genresId: Int) {
        viewModelScope.launch {
            _topChart.value = moviesRepository.getTVSeriesByGenreID(genresId)
        }
    }
}