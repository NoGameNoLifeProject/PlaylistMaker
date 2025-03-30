package com.practicum.playlistmaker.media.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.media.domain.api.IImageRepository
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.api.IPlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractor(private val playlistRepository: IPlaylistRepository, private val imageRepository: IImageRepository) : IPlaylistInteractor {
    override suspend fun cretePlaylist(playlist: Playlist) {
        playlistRepository.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylistData(
        playlistId: Long,
        name: String?,
        description: String?,
        cover: Uri?
    ) {
        playlistRepository.updatePlaylistData(playlistId, name, description, cover)
    }

    override fun getPlaylists(): Flow<List<PlaylistWithTracks>> {
        return playlistRepository.getPlaylists()
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override fun getPlaylist(playlist: Playlist): Flow<PlaylistWithTracks> {
        return playlistRepository.getPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track) {
        playlistRepository.addTrackToPlaylist(playlistId, track)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, track: Track) {
        playlistRepository.removeTrackFromPlaylist(playlistId, track)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): Uri {
        return imageRepository.saveToPrivateStorage(uri)
    }

    override suspend fun sharePlaylist(playlist: Playlist) {
        playlistRepository.sharePlaylist(playlist)
    }
}