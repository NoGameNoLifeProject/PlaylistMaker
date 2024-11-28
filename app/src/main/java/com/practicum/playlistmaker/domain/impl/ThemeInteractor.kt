package com.practicum.playlistmaker.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.api.IThemeInteractor
import com.practicum.playlistmaker.domain.repository.ISettingsRepository

const val PREFERENCES_DARK_THEME = "dark_theme"
class ThemeInteractor(private val repository: ISettingsRepository) : IThemeInteractor{
    override fun setTheme(dark: Boolean) {
        repository.setBoolean(PREFERENCES_DARK_THEME, dark)
        AppCompatDelegate.setDefaultNightMode(
            if (dark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun getTheme(consumer: IThemeInteractor.IThemeConsumer){
        consumer.consume(repository.getBoolean(PREFERENCES_DARK_THEME))
    }
}