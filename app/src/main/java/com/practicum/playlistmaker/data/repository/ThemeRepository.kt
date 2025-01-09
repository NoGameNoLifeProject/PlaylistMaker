package com.practicum.playlistmaker.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.repository.IThemeRepository

const val PREFERENCES_DARK_THEME = "dark_theme"
class ThemeRepository(context: Context) : IThemeRepository {
    private val sharedPref = context.getSharedPreferences(PREFERENCES_DARK_THEME, Context.MODE_PRIVATE)

    override fun setTheme(dark: Boolean) {
        sharedPref.edit().putBoolean(PREFERENCES_DARK_THEME, dark).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (dark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun getTheme(): Boolean {
        return sharedPref.getBoolean(PREFERENCES_DARK_THEME, false)
    }
}