package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface ISearchHistoryInteractor {
    fun addSearchHistory(track: Track)
    fun getSearchHistory(consumer: ISearchHistoryConsumer)
    fun clearSearchHistory()

    fun interface ISearchHistoryConsumer {
        fun consume(tracks: List<Track>)
    }
}