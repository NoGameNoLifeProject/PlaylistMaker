package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.INetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.models.IResource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.ITracksRepository

class TracksRepository(private val networkClient: INetworkClient) : ITracksRepository {
    override fun searchTracks(query: String): IResource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(query))
        if (response.resultCode == 200) {
            val tracks = (response as TracksSearchResponse).results.map {
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
            }
            return IResource.Success(tracks)
        } else if (response.resultCode == -1) {
            return IResource.NetworkError(response.message)
        } else {
            return IResource.Error(response.message)
        }
    }

}