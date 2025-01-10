package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface ITracksInteractor {
    fun searchTracks(query: String, consumer: ITracksConsumer)

    fun interface ITracksConsumer {
        fun consume(tracks: List<Track>)
    }
}