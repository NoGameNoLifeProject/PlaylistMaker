package com.practicum.playlistmaker.media.ui.view_models

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.CreatePlaylistScreenState
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

open class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistInteractor: IPlaylistInteractor
) : ViewModel() {
    private val _playlistWithTracks = MutableLiveData<PlaylistWithTracks>()
    private val _showEmptyMessage = SingleLiveEvent<Boolean>()
    private val _hidePlaylistEditBottomSheet = SingleLiveEvent<Boolean>()
    private val _navigateBack = SingleLiveEvent<Boolean>()

    val playlistWithTracks: LiveData<PlaylistWithTracks> get() = _playlistWithTracks
    val showEmptyMessage: LiveData<Boolean> get() = _showEmptyMessage
    val hidePlaylistEditBottomSheet: LiveData<Boolean> get() = _hidePlaylistEditBottomSheet
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    init {
        updatePlaylist()
    }

    fun removeTrack(track: Track){
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(
                playlistId,
                track
            )
        }
        updatePlaylist()
    }

    fun share(){
        playlistWithTracks.value?.let {
            if (it.tracks.isEmpty())
                _showEmptyMessage.value = true
            else{
                viewModelScope.launch {
                    playlistInteractor.sharePlaylist(it.playlist)
                    _hidePlaylistEditBottomSheet.value = true
                }
            }
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId).first()
            playlistInteractor.deletePlaylist(playlist)
            _navigateBack.value = true
        }
    }

    fun updatePlaylist() {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId).first()
            _playlistWithTracks.value = playlistInteractor.getPlaylist(playlist).first()
        }
    }
}