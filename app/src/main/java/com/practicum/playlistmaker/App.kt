package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES_SETTINGS = "settings"
const val PREFERENCES_DARK_THEME = "dark_theme"

class App : Application() {
    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        switchTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean? = null) {
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_SETTINGS, MODE_PRIVATE)
        val savedThemeVal = sharedPrefs.getBoolean(PREFERENCES_DARK_THEME, false)
        darkTheme = darkThemeEnabled ?: savedThemeVal

        if (darkTheme != savedThemeVal)
            sharedPrefs.edit()
                .putBoolean(PREFERENCES_DARK_THEME, darkTheme)
                .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}