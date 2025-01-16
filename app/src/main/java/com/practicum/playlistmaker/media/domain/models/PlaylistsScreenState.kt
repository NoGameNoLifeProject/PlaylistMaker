package com.practicum.playlistmaker.media.domain.models

sealed class PlaylistsScreenState {
    object Content : PlaylistsScreenState()
    object Error : PlaylistsScreenState()
}