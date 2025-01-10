package com.practicum.playlistmaker.settings.domain.repository

import com.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface IThemeRepository {
    fun setTheme(theme: ThemeSettings)
    fun getTheme(): ThemeSettings
}