package com.practicum.playlistmaker.media.domain.models

import android.net.Uri

data class Playlist(
    val playlistId: Long = 0,
    val name: String,
    val description: String,
    val cover: Uri?
)