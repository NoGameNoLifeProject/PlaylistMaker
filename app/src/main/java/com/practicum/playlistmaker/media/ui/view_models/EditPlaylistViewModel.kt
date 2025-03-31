package com.practicum.playlistmaker.media.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.CreatePlaylistScreenState
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: IPlaylistInteractor
) : CreatePlaylistViewModel(playlistInteractor) {
    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    init {
        viewModelScope.launch {
            _playlist.value = playlistInteractor.getPlaylistById(playlistId).first()
            _canCreatePlaylist.value = true
        }
    }

    override fun onCreateHandler() {
        if (_name.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val savedImagePath = _imageUri?.let { uri ->
                playlistInteractor.saveImageToPrivateStorage(uri)
            }

            playlistInteractor.updatePlaylistData(
                playlistId,
                _name,
                _description,
                savedImagePath
            )
            _screenState.postValue(CreatePlaylistScreenState.NavigateBack)
        }
    }

    override fun onBackHandler() {
        _screenState.postValue(CreatePlaylistScreenState.NavigateBack)
    }
}