package com.practicum.playlistmaker.media.domain.models

sealed class CreatePlaylistScreenState{
    data object Base : CreatePlaylistScreenState()
    data object NavigateBack : CreatePlaylistScreenState()
    data object ShowDialogBeforeNavigateBack : CreatePlaylistScreenState()
    data class PlaylistScreenCreated(val playlistName: String) : CreatePlaylistScreenState()
}
