package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.ITracksInteractor
import com.practicum.playlistmaker.search.domain.models.IResource
import com.practicum.playlistmaker.search.domain.models.ISearchResult
import com.practicum.playlistmaker.search.domain.repository.ITracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractor(private val repository: ITracksRepository) : ITracksInteractor {
    override fun searchTracks(query: String): Flow<ISearchResult> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is IResource.Error -> ISearchResult.Error(result.message)
                is IResource.NetworkError -> ISearchResult.NetworkError(result.message)
                is IResource.Success -> ISearchResult.Data(result.data)
            }
        }
    }
}