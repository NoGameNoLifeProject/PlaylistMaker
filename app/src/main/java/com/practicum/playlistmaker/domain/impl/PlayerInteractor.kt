package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.domain.models.EPlayerState
import com.practicum.playlistmaker.domain.repository.IPlayerRepository

class PlayerInteractor(private val repository: IPlayerRepository): IPlayerInteractor {
    override fun prepare(url: String) {
        repository.prepare(url)
    }

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }

    override fun getCurrentPosition(): Long {
        return repository.getCurrentPosition()
    }

    override fun getState(): EPlayerState {
        return repository.getState()
    }

    override fun setOnPreparedListener(callback: () -> Unit) {
        repository.setOnPreparedListener(callback)
    }

    override fun setOnCompletionListener(callback: () -> Unit) {
        repository.setOnCompletionListener(callback)
    }

    override fun setOnErrorListener(callback: () -> Unit) {
        repository.setOnErrorListener(callback)
    }
}