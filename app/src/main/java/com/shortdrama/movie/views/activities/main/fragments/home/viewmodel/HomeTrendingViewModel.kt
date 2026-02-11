package com.shortdrama.movie.views.activities.main.fragments.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.core_api_storage.common.toUIModel
import com.module.core_api_storage.database.DramaRepository
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeTrendingViewModel @Inject constructor(
    private val dramaRepository: DramaRepository
) : ViewModel() {

    init {
        Log.e("TAG", "init HomeTrendingViewModel")
    }

    //banner home popular
    private val _banner = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val banner: StateFlow<List<DramaWithGenresUIModel>> = _banner.asStateFlow()

    //popular
    private val _popular = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val popular: StateFlow<List<DramaWithGenresUIModel>> = _popular.asStateFlow()

    //new release
    private val _newRelease = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val newRelease: StateFlow<List<DramaWithGenresUIModel>> = _newRelease.asStateFlow()

    //new release
    private val _topChart = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val topChart: StateFlow<List<DramaWithGenresUIModel>> = _topChart.asStateFlow()


    fun loadMovieBanner() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category1")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _banner.value = uiList
                }
        }
    }

    fun loadMoviePopular() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category2")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _popular.value = uiList
                }
        }
    }

    fun loadMovieNewRelease() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category1")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _newRelease.value = uiList
                }
        }
    }

    fun loadMovieTopChart() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category1")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _topChart.value = uiList
                }
        }
    }
}