package com.practicum.playlistmaker.media.data.db.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("SELECT * FROM track_table order by id desc")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Long>
}