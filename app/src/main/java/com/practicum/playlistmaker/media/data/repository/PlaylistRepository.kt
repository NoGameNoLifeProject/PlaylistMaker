package com.practicum.playlistmaker.media.data.repository

import com.practicum.playlistmaker.media.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmaker.media.data.converters.PlaylistWithTracksDbConverter
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.media.domain.api.IPlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepository(
    private val dataBase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConverter,
    private val playlistWithTracksDbConverter: PlaylistWithTracksDbConverter
) : IPlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        dataBase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        dataBase.playlistDao().deletePlaylist(playlistEntity)
        dataBase.playlistTrackCrossRefDao()
            .deletePlaylistFromPlaylistTrackCrossRef(playlistEntity.playlistId)
    }

    override fun getPlaylists(): Flow<List<PlaylistWithTracks>> {
        return dataBase.playlistDao().getPlaylistsWithTracks().map { playlists ->
            playlists.map { playlist -> playlistWithTracksDbConverter.map(playlist) }
        }
    }

    override fun getPlaylist(playlist: Playlist): Flow<PlaylistWithTracks> {
        return dataBase.playlistDao().getPlaylistWithTracks(playlist.playlistId)
            .map { playlistWithTracksDbConverter.map(it) }
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track) {
        val trackEntity = playlistTrackDbConverter.map(track)
        dataBase.playlistTrackDao().insertTrack(trackEntity)
        dataBase.playlistTrackCrossRefDao().insertPlaylistTrackCrossRef(
            PlaylistTrackCrossRef(
                playlistId, trackEntity.trackId
            )
        )
    }


    override suspend fun removeTrackFromPlaylist(playlistId: Long, track: Track) {
        val trackEntity = playlistTrackDbConverter.map(track)
        dataBase.playlistTrackCrossRefDao()
            .deleteTrackFromPlaylistTrackCrossRef(playlistId, trackEntity.trackId)
    }
}