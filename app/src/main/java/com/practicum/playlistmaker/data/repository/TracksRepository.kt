package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.ITracksRepository

class TracksRepository(private val networkClient: NetworkClient) : ITracksRepository {
    override fun searchTracks(query: String): List<Track> {
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
            return tracks
        } else {
            return emptyList()
        }
    }

}