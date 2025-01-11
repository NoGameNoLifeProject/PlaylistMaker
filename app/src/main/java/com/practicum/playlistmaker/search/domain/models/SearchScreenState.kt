package com.practicum.playlistmaker.search.domain.models

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class Content(val tracks: List<Track>) : SearchScreenState()
    object History : SearchScreenState()
    object Empty : SearchScreenState()
    object Error : SearchScreenState()
}