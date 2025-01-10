package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.IThemeInteractor
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings
import com.practicum.playlistmaker.settings.domain.repository.IThemeRepository

class ThemeInteractor(private val repository: IThemeRepository) : IThemeInteractor {
    override fun setTheme(theme: ThemeSettings) {
        repository.setTheme(theme)
    }

    override fun getTheme(consumer: IThemeInteractor.IThemeConsumer){
        consumer.consume(repository.getTheme())
    }
}