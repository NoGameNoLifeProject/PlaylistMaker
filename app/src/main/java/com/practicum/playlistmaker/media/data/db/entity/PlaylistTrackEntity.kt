package com.practicum.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks",
    indices = [Index(value = ["createdAt"])]
)
data class PlaylistTrackEntity(
    @PrimaryKey
    val trackId: Long,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Long,
    val previewUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)