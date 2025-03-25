package com.practicum.playlistmaker.media.domain.models

import com.practicum.playlistmaker.search.domain.models.Track

data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
)