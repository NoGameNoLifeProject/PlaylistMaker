package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface ITracksInteractor {
    fun searchTracks(query: String, consumer: ITracksConsumer)

    fun interface ITracksConsumer {
        fun consume(tracks: List<Track>)
    }
}