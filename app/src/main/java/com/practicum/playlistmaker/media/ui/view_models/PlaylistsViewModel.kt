package com.practicum.playlistmaker.media.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.media.domain.models.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: IPlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Empty)
    val state: LiveData<PlaylistsScreenState> get() = _state

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists -> processResult(playlists) }
        }
    }

    private fun processResult(playlists: List<PlaylistWithTracks>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistsScreenState.Empty)
        } else {
            renderState(PlaylistsScreenState.Content(playlists))
        }
    }

    private fun renderState(content: PlaylistsScreenState) {
        _state.postValue(content)
    }
}