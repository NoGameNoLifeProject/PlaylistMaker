package com.practicum.playlistmaker.media.domain.models

sealed class FavoritesScreenState {
    object Content : FavoritesScreenState()
    object Error : FavoritesScreenState()
}