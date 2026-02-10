package com.shortdrama.movie.views.activities.main.fragments.for_you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.core_api.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    //get movie_series paging
    val tvSeriesPagingFlow =
        moviesRepository.getTVSeriesPaging()
            .cachedIn(viewModelScope)
}