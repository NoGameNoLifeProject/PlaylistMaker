package com.practicum.playlistmaker.search.domain.models

sealed class SearchState {
    object Loading : SearchState()
    data class Content(val tracks: List<Track>) : SearchState()
    object History : SearchState()
    object Empty : SearchState()
    object Error : SearchState()
}