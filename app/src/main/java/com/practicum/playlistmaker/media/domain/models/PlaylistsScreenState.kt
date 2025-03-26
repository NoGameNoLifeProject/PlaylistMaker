package com.practicum.playlistmaker.media.domain.models

sealed class PlaylistsScreenState {
    data class Content(val playlists: List<PlaylistWithTracks>) : PlaylistsScreenState()
    data object Empty : PlaylistsScreenState()
}