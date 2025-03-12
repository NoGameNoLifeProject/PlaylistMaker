package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ISearchResult
import kotlinx.coroutines.flow.Flow

interface ITracksInteractor {
    fun searchTracks(query: String): Flow<ISearchResult>
}