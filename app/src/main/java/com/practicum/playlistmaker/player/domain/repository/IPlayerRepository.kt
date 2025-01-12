package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.domain.models.EPlayerState

interface IPlayerRepository {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Long

    fun setOnStateChangeListener(callback: (state: EPlayerState) -> Unit)
}