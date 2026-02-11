package com.shortdrama.movie.views.activities.main.fragments.home.viewmodel

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
class HomeRankingViewModel @Inject constructor(
    private val dramaRepository: DramaRepository
) : ViewModel() {
    private val _ranking = MutableStateFlow<List<DramaWithGenresUIModel>>(emptyList())
    val ranking = _ranking.asStateFlow()
    fun loadRanking() {
        viewModelScope.launch {
            dramaRepository.getAllDramaByCategoryWithGenres("category3")
                .collect { list ->
                    val uiList = list.map { it.toUIModel() }
                    _ranking.value = uiList
                }
        }
    }
}