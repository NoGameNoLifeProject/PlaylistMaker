package com.practicum.playlistmaker.media.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.domain.models.FavoritesScreenState

class FavoritesViewModel : ViewModel() {
    private val _state = MutableLiveData<FavoritesScreenState>(FavoritesScreenState.Error)
    val state: LiveData<FavoritesScreenState>
        get() = _state
}