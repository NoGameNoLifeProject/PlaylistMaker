package com.practicum.playlistmaker.media.data.converters

import androidx.core.net.toUri
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            name = playlist.name,
            description = playlist.description,
            cover = playlist.cover.toString(),
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlistEntity.playlistId,
            name = playlistEntity.name,
            description = playlistEntity.description,
            cover = playlistEntity.cover?.toUri(),
        )
    }
}
