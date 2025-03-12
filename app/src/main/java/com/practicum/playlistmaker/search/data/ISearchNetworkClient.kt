package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.Response

interface ISearchNetworkClient {
    suspend fun searchTracks(query: String): Response
}