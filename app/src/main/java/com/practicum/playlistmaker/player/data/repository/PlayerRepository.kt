package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.models.EPlayerState
import com.practicum.playlistmaker.player.domain.repository.IPlayerRepository

class PlayerRepository : IPlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var onPreparedListener: (() -> Unit)? = null
    private var onCompletionListener: (() -> Unit)? = null
    private var onErrorListener: (() -> Unit)? = null

    private var state: EPlayerState = EPlayerState.DEFAULT

    override fun prepare(url: String) {
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                onPreparedListener?.invoke()
            }
            mediaPlayer.setOnCompletionListener {
                onCompletionListener?.invoke()
            }
        } catch (e: Exception) {
            onErrorListener?.invoke()
        }
    }

    override fun play() {
        mediaPlayer.start()
        state = EPlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        state = EPlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getState(): EPlayerState {
        return state
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun setOnPreparedListener(callback: () -> Unit) {
        state = EPlayerState.PREPARED
        onPreparedListener = callback
    }

    override fun setOnCompletionListener(callback: () -> Unit) {
        state = EPlayerState.PREPARED
        onCompletionListener = callback
    }

    override fun setOnErrorListener(callback: () -> Unit) {
        onErrorListener = callback
    }
}