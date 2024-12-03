package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface ITracksRepository {
    fun searchTracks(query: String): List<Track>
}