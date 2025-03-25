package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.db.entity.PlaylistTrackCrossRef

@Dao
interface PlaylistTrackCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrackCrossRef(playlistTrackCrossRefEntity: PlaylistTrackCrossRef)

    @Query("DELETE FROM PlaylistTrackCrossRef WHERE playlistId =:playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylistTrackCrossRef(playlistId: Long, trackId: Long)

    @Query("DELETE FROM PlaylistTrackCrossRef WHERE playlistId = :playlistId")
    suspend fun deletePlaylistFromPlaylistTrackCrossRef(playlistId: Long)
}