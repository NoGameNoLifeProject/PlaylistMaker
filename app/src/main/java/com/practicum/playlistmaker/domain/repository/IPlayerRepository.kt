package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.EPlayerState

interface IPlayerRepository {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Long
    fun getState(): EPlayerState

    fun setOnPreparedListener(callback: () -> Unit)
    fun setOnCompletionListener(callback: () -> Unit)
    fun setOnErrorListener(callback: () -> Unit)
}