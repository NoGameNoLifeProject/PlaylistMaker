package com.practicum.playlistmaker.media.data.repository

import com.practicum.playlistmaker.media.data.converters.TrackDbConverter
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.domain.api.IFavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(private val appDatabase: AppDatabase, private val trackDbConverter: TrackDbConverter): IFavoritesRepository {
    override fun favoritesTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks().map { trackEntities ->
            trackEntities.map { track -> trackDbConverter.map(track) }
        }
    }

    override suspend fun addFavoriteTrack(track: Track) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun removeFavoriteTrack(trackId: Long) {
        appDatabase.trackDao().deleteTrackById(trackId)
    }
}