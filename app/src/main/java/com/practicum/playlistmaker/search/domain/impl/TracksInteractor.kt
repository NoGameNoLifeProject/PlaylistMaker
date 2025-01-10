package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.ITracksInteractor
import com.practicum.playlistmaker.search.domain.models.IResource
import com.practicum.playlistmaker.search.domain.models.ISearchResult
import com.practicum.playlistmaker.search.domain.repository.ITracksRepository
import java.util.concurrent.Executors

class TracksInteractor(private val repository: ITracksRepository) : ITracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(query: String, consumer: ITracksInteractor.ITracksConsumer) {
        executor.execute {
            when (val res = repository.searchTracks(query)) {
                is IResource.Error -> consumer.consume(ISearchResult.Error(res.message))
                is IResource.NetworkError -> consumer.consume(ISearchResult.NetworkError(res.message))
                is IResource.Success -> consumer.consume(ISearchResult.Data(res.data))
            }
        }
    }
}