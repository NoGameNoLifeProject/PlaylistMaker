package com.practicum.playlistmaker.media.domain.api

import android.net.Uri
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface IPlaylistInteractor {
    suspend fun cretePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<PlaylistWithTracks>>
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track)
    suspend fun saveImageToPrivateStorage(uri: Uri): Uri
}