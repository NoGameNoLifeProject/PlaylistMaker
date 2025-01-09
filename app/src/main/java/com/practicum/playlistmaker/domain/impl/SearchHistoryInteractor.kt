package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.ISearchHistoryRepository

class SearchHistoryInteractor(private val repository: ISearchHistoryRepository) :
    ISearchHistoryInteractor {
    override fun addSearchHistory(track: Track) {
        repository.addSearchHistory(track)
    }

    override fun getSearchHistory(consumer: ISearchHistoryInteractor.ISearchHistoryConsumer) {
        consumer.consume(repository.getSearchHistory())
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
}