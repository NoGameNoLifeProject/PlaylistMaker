package com.practicum.playlistmaker.media.ui.view_models

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.CreatePlaylistScreenState
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: IPlaylistInteractor) : ViewModel() {
    private var _imageUri: Uri? = null
    private var _name: String = ""
    private var _description: String = ""
    private val _canCreatePlaylist = MutableLiveData(false)
    private val _screenState = MutableLiveData<CreatePlaylistScreenState>(CreatePlaylistScreenState.Base)

    val canCreatePlaylist: LiveData<Boolean> get() = _canCreatePlaylist
    val screenState: LiveData<CreatePlaylistScreenState> get() = _screenState


    fun onNameChanged(name: String) {
        _name = name
        _canCreatePlaylist.postValue(name.isNotEmpty())
    }

    fun onDescriptionChanged(description: String) {
        _description = description
    }

    fun resetScreenState() {
        _screenState.postValue(CreatePlaylistScreenState.Base)
    }

    fun setImageUri(uri: Uri) {
        _imageUri = uri
    }

    fun onCreateHandler() {
        if (_name.isEmpty()) {
            return
        }

        viewModelScope.launch {
            val savedImagePath = _imageUri?.let { uri ->
                playlistInteractor.saveImageToPrivateStorage(uri)
            }

            val playlist = Playlist(
                name = _name,
                description = _description,
                cover = savedImagePath,
            )
            playlistInteractor.cretePlaylist(playlist)
            _screenState.postValue(CreatePlaylistScreenState.PlaylistScreenCreated(playlist.name))
        }
    }

    fun onBackHandler() {
        if (_name.isEmpty() && _description.isEmpty() && _imageUri == null) {
            _screenState.postValue(CreatePlaylistScreenState.NavigateBack)
        } else {
            _screenState.postValue(CreatePlaylistScreenState.ShowDialogBeforeNavigateBack)
        }
    }
}