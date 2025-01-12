package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.IResource
import com.practicum.playlistmaker.search.domain.models.Track

interface ITracksRepository {
    fun searchTracks(query: String): IResource<List<Track>>
}