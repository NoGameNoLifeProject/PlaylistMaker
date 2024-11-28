package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ITracksInteractor
import com.practicum.playlistmaker.domain.repository.ITracksRepository
import java.util.concurrent.Executors

class TracksInteractor(private val repository: ITracksRepository) : ITracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(query: String, consumer: ITracksInteractor.ITracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(query))
        }
    }
}