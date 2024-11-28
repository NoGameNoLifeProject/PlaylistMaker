package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.repository.IPlayerRepository

class PlayerRepository : IPlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var onPreparedListener: (() -> Unit)? = null
    private var onCompletionListener: (() -> Unit)? = null
    private var onErrorListener: (() -> Unit)? = null

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
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun setOnPreparedListener(callback: () -> Unit) {
        onPreparedListener = callback
    }

    override fun setOnCompletionListener(callback: () -> Unit) {
        onCompletionListener = callback
    }

    override fun setOnErrorListener(callback: () -> Unit) {
        onErrorListener = callback
    }
}