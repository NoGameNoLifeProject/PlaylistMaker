package com.practicum.playlistmaker.settings.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings
import com.practicum.playlistmaker.settings.domain.repository.IThemeRepository
import com.practicum.playlistmaker.utils.Const.PREFERENCES_DARK_THEME

class ThemeRepository(private val sharedPref: SharedPreferences, private val context: Context) : IThemeRepository {
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