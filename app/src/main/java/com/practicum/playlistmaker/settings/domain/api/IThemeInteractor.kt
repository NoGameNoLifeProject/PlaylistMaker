package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface IThemeInteractor {
    fun setTheme(theme: ThemeSettings)
    fun getTheme(consumer: IThemeConsumer)

    fun interface IThemeConsumer {
        fun consume(value: ThemeSettings)
    }
}