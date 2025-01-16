package com.practicum.playlistmaker.media.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.domain.models.PlaylistsScreenState

class PlaylistsViewModel : ViewModel() {
    private val _state = MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Error)
    val state: LiveData<PlaylistsScreenState>
        get() = _state
}