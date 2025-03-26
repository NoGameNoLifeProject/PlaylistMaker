package com.practicum.playlistmaker.media.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.media.domain.api.IImageRepository
import com.practicum.playlistmaker.utils.Const.PLAYLISTS_COVERS_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ImageRepository(private val context: Context) : IImageRepository {
    override suspend fun saveToPrivateStorage(uri: Uri): Uri {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLISTS_COVERS_PATH
        )
        if (!filePath.exists()) {
            filePath.mkdir()
        }

        val file = File(filePath, UUID.randomUUID().toString() + ".jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 70, outputStream)

        return Uri.fromFile(file)
    }
}