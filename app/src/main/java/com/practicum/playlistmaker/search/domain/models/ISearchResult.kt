package com.practicum.playlistmaker.search.domain.models

sealed interface ISearchResult {
    data class Data(val data: List<Track>) : ISearchResult
    data class Error(val message: String) : ISearchResult
    data class NetworkError(val message: String) : ISearchResult
}