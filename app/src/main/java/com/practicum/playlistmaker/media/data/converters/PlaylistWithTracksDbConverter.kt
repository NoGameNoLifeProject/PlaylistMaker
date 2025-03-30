package com.practicum.playlistmaker.media.data.converters

import com.practicum.playlistmaker.media.data.db.entity.PlaylistWithTracksEntity
import com.practicum.playlistmaker.media.data.db.entity.RawPlaylistWithTracksEntity
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks

class PlaylistWithTracksDbConverter(private val playlistDbConverter: PlaylistDbConverter, private val playlistTrackDbConverter: PlaylistTrackDbConverter) {
    fun map(playlist: PlaylistWithTracks): PlaylistWithTracksEntity {
        return PlaylistWithTracksEntity(
            playlist = playlistDbConverter.map(playlist.playlist),
            tracks = playlist.tracks.map { playlistTrackDbConverter.map(it) },
        )
    }

    fun map(playlistEntity: PlaylistWithTracksEntity): PlaylistWithTracks {
        return PlaylistWithTracks(
            playlist = playlistDbConverter.map(playlistEntity.playlist),
            tracks = playlistEntity.tracks.map { playlistTrackDbConverter.map(it) },
        )
    }

    fun map(rawData: List<RawPlaylistWithTracksEntity>): PlaylistWithTracks {
        return PlaylistWithTracks(
            playlist = playlistDbConverter.map(rawData[0].playlist),
            tracks = rawData.filter { it.track != null }.map { playlistTrackDbConverter.map(it.track!!) },
        )
    }
}
