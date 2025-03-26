package com.practicum.playlistmaker.media.domain.api

import android.net.Uri

interface IImageRepository {
    suspend fun saveToPrivateStorage(uri: Uri): Uri
}