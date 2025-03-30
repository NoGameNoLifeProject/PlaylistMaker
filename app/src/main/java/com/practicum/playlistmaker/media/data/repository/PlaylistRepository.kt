package com.practicum.playlistmaker.media.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.media.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.media.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmaker.media.data.converters.PlaylistWithTracksDbConverter
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.media.domain.api.IPlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.TextUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class PlaylistRepository(
    private val context: Context,
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

    override suspend fun updatePlaylistData(
        playlistId: Long,
        name: String?,
        description: String?,
        cover: Uri?
    ) {
        val playlist = getPlaylistById(playlistId).first()
        val updatedPlaylist = playlist.copy(
            name = name ?: playlist.name,
            description = description ?: playlist.description,
            cover = cover ?: playlist.cover
        )
        dataBase.playlistDao().updatePlaylist(playlistDbConverter.map(updatedPlaylist))
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

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return dataBase.playlistDao().getPlaylistById(playlistId)
            .map { playlistDbConverter.map(it) }
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

        val playlists = dataBase.playlistTrackCrossRefDao().getTrackPlaylists(track.trackId)
        if (playlists.isEmpty()) {
            dataBase.playlistTrackDao().deleteTrack(trackEntity)
        }
    }

    override suspend fun sharePlaylist(playlist: Playlist) {
        val playlistWithTracks = getPlaylist(playlist).first()
        val playlistToSend = mutableListOf<String>()
        playlistToSend.add(playlistWithTracks.playlist.name)
        playlistToSend.add(playlistWithTracks.playlist.description)
        playlistToSend.add(
            "${playlistWithTracks.tracks.count()} ${
                TextUtils.getTracksCountEnding(
                    context,
                    playlistWithTracks.tracks.count()
                )
            }"
        )
        playlistWithTracks.tracks.forEachIndexed { index, track ->
            playlistToSend.add("${index + 1}. ${track.trackName} - ${track.artistName} (${track.getTrackLength()})")
        }

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, playlistToSend.joinToString("\n"))
        }
        val shareIntent = Intent.createChooser(intent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}