package com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import com.shortdrama.movie.data.repository.MovieFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val movieFavoriteRepository: MovieFavoriteRepository
) : ViewModel() {
    private val _favouriteMovies = MutableStateFlow<List<MovieFavoriteEntity>>(emptyList())
    val favouriteMovies: StateFlow<List<MovieFavoriteEntity>> = _favouriteMovies.asStateFlow()

    init {
        viewModelScope.launch {
            movieFavoriteRepository.getAllFavouriteMovies().collect { list ->
                _favouriteMovies.value = list
            }
        }
    }

    fun addToFavourite(movie: MovieFavoriteEntity) {
        viewModelScope.launch {
            movieFavoriteRepository.addFavouriteMovie(movie)
            // _uiEvent.emit(MovieUiEvent.ShowToast("Đã thêm vào yêu thích"))
        }
    }

    fun removeFromFavourite(movieId: String) {
        viewModelScope.launch {
            movieFavoriteRepository.deleteFavouriteMovie(movieId)
        }
    }
}