package com.practicum.playlistmaker.settings.data.repository

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings
import com.practicum.playlistmaker.settings.domain.repository.IThemeRepository

const val PREFERENCES_DARK_THEME = "dark_theme"
class ThemeRepository(val context: Context) : IThemeRepository {
    private val sharedPref = context.getSharedPreferences(PREFERENCES_DARK_THEME, Context.MODE_PRIVATE)

    override fun setTheme(theme: ThemeSettings) {
        sharedPref.edit().putBoolean(PREFERENCES_DARK_THEME, theme.isDark).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (theme.isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun getTheme(): ThemeSettings {
        return ThemeSettings(sharedPref.getBoolean(PREFERENCES_DARK_THEME, isSystemInDarkTheme()))
    }

    private fun isSystemInDarkTheme(): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}