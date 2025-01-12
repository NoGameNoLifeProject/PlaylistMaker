package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.models.EPlayerState
import com.practicum.playlistmaker.player.domain.repository.IPlayerRepository

class PlayerRepository(private val mediaPlayer: MediaPlayer) : IPlayerRepository {
    private var onStateChangeListener: ((EPlayerState) -> Unit)? = null

    private var state: EPlayerState = EPlayerState.DEFAULT

    override fun prepare(url: String) {
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                updateState(EPlayerState.PREPARED)
            }
            mediaPlayer.setOnCompletionListener {
                updateState(EPlayerState.PREPARED)
            }
        } catch (e: Exception) {
            updateState(EPlayerState.ERROR)
        }
    }

    override fun play() {
        mediaPlayer.start()
        updateState(EPlayerState.PLAYING)
    }

    override fun pause() {
        mediaPlayer.pause()
        updateState(EPlayerState.PAUSED)
    }

    override fun release() {
        mediaPlayer.release()
    }

    private fun updateState(newState: EPlayerState) {
        state = newState
        onStateChangeListener?.invoke(state)
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun setOnStateChangeListener(callback: (state: EPlayerState) -> Unit) {
        onStateChangeListener = callback
    }
}