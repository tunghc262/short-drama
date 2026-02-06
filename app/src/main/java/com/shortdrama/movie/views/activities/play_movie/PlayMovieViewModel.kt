package com.shortdrama.movie.views.activities.play_movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_api.model.ui.TVSeriesEpisodeUiModel
import com.example.core_api.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlayMovieViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    // list episodes
    private val _listEpisodes = MutableStateFlow<TVSeriesEpisodeUiModel?>(null)
    val listEpisodes: StateFlow<TVSeriesEpisodeUiModel?> = _listEpisodes.asStateFlow()
    //get episodes by series_id
    fun loadEpisodes(id: Int, numberSeason: Int) {
        Log.e("TAG_EPISODE_API", "loadEpisodes: 1")
        viewModelScope.launch {
            _listEpisodes.value = moviesRepository.getTVSeriesEpisodes(id, numberSeason)
        }
    }
}