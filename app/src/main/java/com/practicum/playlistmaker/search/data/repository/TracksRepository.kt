package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.ISearchNetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.models.IResource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.ITracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepository(private val networkClient: ISearchNetworkClient) : ITracksRepository {
    override fun searchTracks(query: String): Flow<IResource<List<Track>>> = flow {
        val response = networkClient.searchTracks(query)
        when (response.resultCode) {
            200 -> {
                with(response as TracksSearchResponse) {
                    emit(IResource.Success(results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }))
                }
            }
            else -> emit(IResource.Error(response.message))
        }
    }

}