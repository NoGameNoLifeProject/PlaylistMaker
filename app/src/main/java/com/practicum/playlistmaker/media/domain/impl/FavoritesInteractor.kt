package com.practicum.playlistmaker.media.domain.impl

import com.practicum.playlistmaker.media.domain.api.IFavoritesInteractor
import com.practicum.playlistmaker.media.domain.api.IFavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractor(private val favoritesRepository: IFavoritesRepository): IFavoritesInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.favoritesTracks()
    }

    override suspend fun addFavoriteTrack(track: Track) {
        favoritesRepository.addFavoriteTrack(track)
    }

    override suspend fun removeFavoriteTrack(trackId: Long) {
        favoritesRepository.removeFavoriteTrack(trackId)
    }
}