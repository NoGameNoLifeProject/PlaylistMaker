package com.practicum.playlistmaker.media.domain.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed class FavoritesScreenState {
    object Loading : FavoritesScreenState()
    data class Content(val tracks: List<Track>) : FavoritesScreenState()
    object Empty : FavoritesScreenState()
}