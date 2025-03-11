package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.IResource
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface ITracksRepository {
    fun searchTracks(query: String): Flow<IResource<List<Track>>>
}