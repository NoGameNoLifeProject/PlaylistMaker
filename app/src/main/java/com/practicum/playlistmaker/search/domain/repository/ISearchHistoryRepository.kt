package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.Track

interface ISearchHistoryRepository {
    suspend fun addSearchHistory(track: Track)
    suspend fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
}