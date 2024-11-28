package com.practicum.playlistmaker.data.repository

import android.content.Context
import com.practicum.playlistmaker.domain.repository.ISettingsRepository

const val SHARED_PREFERENCES_SETTINGS = "settings"
class SettingsRepository(context: Context) : ISettingsRepository {
    private val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_SETTINGS, Context.MODE_PRIVATE)

    override fun setBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }
}