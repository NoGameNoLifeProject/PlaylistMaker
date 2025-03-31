package com.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media.data.db.dao.PlaylistTrackCrossRefDao
import com.practicum.playlistmaker.media.data.db.dao.PlaylistTrackDao
import com.practicum.playlistmaker.media.data.db.dao.TrackDao
import com.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity

@Database(
    version = 3,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackCrossRef::class, PlaylistTrackEntity::class],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackCrossRefDao(): PlaylistTrackCrossRefDao
}