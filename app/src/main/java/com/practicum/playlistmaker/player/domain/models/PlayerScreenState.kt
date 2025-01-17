package com.practicum.playlistmaker.player.domain.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed class PlayerScreenState {
    object Loading : PlayerScreenState()
    data class Content(val track: Track, val currentTrackTime: String, val playButtonState: Int) : PlayerScreenState()
    data class Error(val track: Track, val currentTrackTime: String, val playButtonState: Int) : PlayerScreenState()
}