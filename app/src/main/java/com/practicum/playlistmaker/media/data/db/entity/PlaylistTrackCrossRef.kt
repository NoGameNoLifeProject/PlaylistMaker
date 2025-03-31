package com.practicum.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["playlistId", "trackId"], indices = [Index(value = ["trackId"]), Index(value = ["linkedAt"])])
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: Long,
    val linkedAt: Long = System.currentTimeMillis()
)