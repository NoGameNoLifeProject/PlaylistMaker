package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ITracksInteractor
import com.practicum.playlistmaker.domain.repository.ITracksRepository

class TracksInteractor(private val repository: ITracksRepository) : ITracksInteractor {
    override fun searchTracks(query: String, consumer: ITracksInteractor.ITracksConsumer) {
        consumer.consume(repository.searchTracks(query))
    }
}