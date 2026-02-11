package com.shortdrama.movie.views.activities.main.fragments.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.core_api_storage.common.toUIModel
import com.module.core_api_storage.database.DramaRepository
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.utils.DataUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeNewViewModel @Inject constructor(
    private val dramaRepository: DramaRepository
) : ViewModel() {
    private val _exclusive = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val exclusive = _exclusive.asStateFlow()
    private val _newRelease = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val newRelease = _newRelease.asStateFlow()

    private val _comingSoon = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val comingSoon = _comingSoon.asStateFlow()

    fun loadNewRelease() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category3")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _newRelease.value = uiList
                }
        }
    }

    fun loadExclusive() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category3")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _exclusive.value = uiList
                }
        }
    }

    fun loadComingSoon() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category3")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    val comingSoonDates = DataUtils.generateComingSoonDates(uiList.size)
                    val listComingSoon = uiList.mapIndexed { index, item ->
                        item.copy(dateComingSoon = comingSoonDates[index])
                    }.sortedBy { it.dateComingSoon }
                    _comingSoon.value = listComingSoon
                }
        }
    }
}