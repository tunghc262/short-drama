package com.shortdrama.movie.views.activities.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.core_api_storage.common.toUIModel
import com.module.core_api_storage.database.DramaRepository
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dramaRepository: DramaRepository
) : ViewModel() {
    private val _drama = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val drama = _drama.asStateFlow()

    private val _dramaTrending = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val dramaTrending = _dramaTrending.asStateFlow()

    fun loadAllDrama() {
        viewModelScope.launch {
            dramaRepository.getAllDramaWithGenres()
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _drama.value = uiList
                }
        }
    }

    fun loadDramaTrending() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category2")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _dramaTrending.value = uiList
                }
        }
    }
}