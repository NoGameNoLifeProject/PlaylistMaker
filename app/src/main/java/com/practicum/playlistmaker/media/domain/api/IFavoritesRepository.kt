package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface IFavoritesRepository {
    fun favoritesTracks(): Flow<List<Track>>

    suspend fun addFavoriteTrack(track: Track)

    suspend fun removeFavoriteTrack(trackId: Long)
}