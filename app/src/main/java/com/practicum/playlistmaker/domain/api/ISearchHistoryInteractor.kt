package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface ISearchHistoryInteractor {
    fun addSearchHistory(track: Track)
    fun getSearchHistory(consumer: ISearchHistoryConsumer)
    fun clearSearchHistory()

    fun interface ISearchHistoryConsumer {
        fun consume(tracks: List<Track>)
    }
}