package com.practicum.playlistmaker.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.repository.ISearchHistoryRepository

const val PREFERENCES_SEARCH_HISTORY = "search_history"
private const val maxHistorySize = 10

class SearchHistoryRepository(context: Context) : ISearchHistoryRepository {
    private val sharedPref = context.getSharedPreferences(PREFERENCES_SEARCH_HISTORY, Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    override fun addSearchHistory(track: Track) {
        val history = getSearchHistory()
        val prevPos = history.indexOfFirst {it.trackId == track.trackId}
        if (prevPos == -1){
            history.add(0, track)
        } else {
            history.removeAt(prevPos)
            history.add(0, track)
        }

        if (history.size > maxHistorySize) {
            history.removeLast()
        }

        val editor = sharedPref.edit()
        editor.putString(PREFERENCES_SEARCH_HISTORY, gson.toJson(history))
        editor.apply()
    }

    override fun getSearchHistory(): MutableList<Track> {
        val jsonString = sharedPref.getString(PREFERENCES_SEARCH_HISTORY, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Track>>() {}.type;
        return gson.fromJson(jsonString, type)
    }

    override fun clearSearchHistory() {
        sharedPref.edit().remove(PREFERENCES_SEARCH_HISTORY).apply()
    }
}