package com.practicum.playlistmaker.media.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.IFavoritesInteractor
import com.practicum.playlistmaker.media.domain.models.FavoritesScreenState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: IFavoritesInteractor) : ViewModel() {
    private val _state = MutableLiveData<FavoritesScreenState>(FavoritesScreenState.Empty)
    val state: LiveData<FavoritesScreenState>
        get() = _state

    private fun renderState(state: FavoritesScreenState) {
        _state.value = state
    }

    fun loadFavorites() {
        viewModelScope.launch {
            renderState(FavoritesScreenState.Loading)
            favoritesInteractor.favoritesTracks().collect { tracks ->
                if (tracks.isEmpty())
                    renderState(FavoritesScreenState.Empty)
                else
                    renderState(FavoritesScreenState.Content(tracks))
            }
        }
    }
}