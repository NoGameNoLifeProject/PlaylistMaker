package com.practicum.playlistmaker.media.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class RawPlaylistWithTracksEntity(
    @Embedded val playlist: PlaylistEntity,
    @Embedded val track: PlaylistTrackEntity?,
    @ColumnInfo(name = "linkedAt") val linkedAt: Long?
)
