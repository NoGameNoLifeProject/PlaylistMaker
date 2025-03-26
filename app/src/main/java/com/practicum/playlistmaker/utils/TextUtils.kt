package com.practicum.playlistmaker.utils

import android.content.Context
import com.practicum.playlistmaker.R

object TextUtils {
    fun getTracksCountEnding(context: Context, count: Int): String {
        return when {
            count % 10 == 1 -> context.getString(R.string.media_playlists_track_one)
            count % 10 in 2..4 -> context.getString(R.string.media_playlists_track_some)
            else -> context.getString(R.string.media_playlists_track_many)
        }
    }
}