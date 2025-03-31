package com.practicum.playlistmaker.utils

import android.content.Context
import com.practicum.playlistmaker.R

object TextUtils {
    fun getTracksCountEnding(context: Context, count: Int): String {
        return when {
            count in 11..14 -> context.getString(R.string.media_playlists_track_many)
            count % 10 == 1 -> context.getString(R.string.media_playlists_track_one)
            count % 10 in 2..4 -> context.getString(R.string.media_playlists_track_some)
            else -> context.getString(R.string.media_playlists_track_many)
        }
    }

    fun getTracksMinEnding(context: Context, count: Int): String {
        return when {
            count in 11..14 -> context.getString(R.string.media_playlist_min_many)
            count % 10 == 1 -> context.getString(R.string.media_playlist_min_one)
            count % 10 in 2..4 -> context.getString(R.string.media_playlist_min_some)
            else -> context.getString(R.string.media_playlist_min_many)
        }
    }
}