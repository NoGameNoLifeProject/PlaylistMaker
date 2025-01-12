package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ISearchResult

interface ITracksInteractor {
    fun searchTracks(query: String, consumer: ITracksConsumer)

    fun interface ITracksConsumer {
        fun consume(data: ISearchResult)
    }
}