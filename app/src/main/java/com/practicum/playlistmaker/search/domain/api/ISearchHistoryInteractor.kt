package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface ISearchHistoryInteractor {
    suspend fun addSearchHistory(track: Track)
    suspend fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}