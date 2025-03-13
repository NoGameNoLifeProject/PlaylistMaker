package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.ISearchHistoryRepository

class SearchHistoryInteractor(private val repository: ISearchHistoryRepository) :
    ISearchHistoryInteractor {
    override suspend fun addSearchHistory(track: Track) {
        repository.addSearchHistory(track)
    }

    override suspend fun getSearchHistory(): List<Track> {
        return repository.getSearchHistory()
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
}