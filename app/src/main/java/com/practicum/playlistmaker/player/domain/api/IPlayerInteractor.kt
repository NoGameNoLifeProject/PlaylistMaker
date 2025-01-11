package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.EPlayerState

interface IPlayerInteractor {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Long

    fun setOnStateChangeListener(callback: (state: EPlayerState) -> Unit)
}