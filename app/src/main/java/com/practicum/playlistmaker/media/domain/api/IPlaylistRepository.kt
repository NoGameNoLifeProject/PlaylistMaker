package com.practicum.playlistmaker.media.domain.api

import android.net.Uri
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface IPlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylistData(playlistId: Long, name: String?, description: String?, cover: Uri?)

    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)
    suspend fun removeTrackFromPlaylist(playlistId: Long, track: Track)

    fun getPlaylistById(playlistId: Long): Flow<Playlist>
    fun getPlaylists(): Flow<List<PlaylistWithTracks>>
    fun getPlaylist(playlist: Playlist): Flow<PlaylistWithTracks>

    suspend fun sharePlaylist(playlist: Playlist)
}