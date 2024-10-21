package com.practicum.playlistmaker.Search

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Models.Track


const val PREFERENCES_SEARCH_HISTORY = "search_history"
private const val maxHistorySize = 10
class SearchHistory(val sharedPrefs: SharedPreferences, private val onTrackClick: (Track) -> Unit) {
    private val gson: Gson = Gson()
    private val tracks: MutableList<Track> = getHistory().toMutableList()

    val searchHistoryAdapter = SearchResultsAdapter(tracks) {
        addTrack(it)
        onTrackClick(it)
    }

    fun addTrack(track: Track){
        val prevPos = tracks.indexOfFirst {it.trackId == track.trackId}
        if (prevPos == -1){
            tracks.add(0, track)
        } else {
            tracks.removeAt(prevPos)
            tracks.add(0, track)
        }

        if (tracks.size > maxHistorySize) {
            tracks.removeLast()
        }

        searchHistoryAdapter.notifyDataSetChanged()
        saveHistory()
    }

    fun saveHistory() {
        val editor = sharedPrefs.edit()
        editor.putString(PREFERENCES_SEARCH_HISTORY, gson.toJson(tracks))
        editor.apply()
    }

    fun getHistory(): List<Track> {
        val jsonString = sharedPrefs.getString(PREFERENCES_SEARCH_HISTORY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type;
        return gson.fromJson(jsonString, type)
    }

    fun isEmpty(): Boolean {
        return tracks.isEmpty()
    }

    fun clearHistory(){
        tracks.clear()
        saveHistory()
        searchHistoryAdapter.notifyDataSetChanged()
    }
}