package com.shortdrama.movie.views.activities.play_movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.core_api_storage.common.toUIModel
import com.module.core_api_storage.database.DramaRepository
import com.module.core_api_storage.model_ui.DramaEpisodeUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlayMovieViewModel @Inject constructor(
    private val dramaRepository: DramaRepository,
) : ViewModel() {
    // list episodes
    private val _listEpisodes = MutableStateFlow<List<DramaEpisodeUIModel>>(emptyList())
    val listEpisodes: StateFlow<List<DramaEpisodeUIModel>> = _listEpisodes.asStateFlow()
    fun loadEpisodes(dramaId: String) {
        viewModelScope.launch {
            Log.e("MOVIEEE", "loadEpisodes: $dramaId")
            dramaRepository.getAllEpisodeByDrama(dramaId)
                .collect { list ->
                    Log.e("MOVIEEE", "loadEpisodes: $dramaId - ${list.size}")
                    val uiList = list.map { it.toUIModel() }.sortedBy { it.serialNo.toInt() }
                    _listEpisodes.value = uiList
                }
        }
    }
}