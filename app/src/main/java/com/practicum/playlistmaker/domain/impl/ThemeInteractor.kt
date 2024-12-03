package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.IThemeInteractor
import com.practicum.playlistmaker.domain.repository.IThemeRepository

class ThemeInteractor(private val repository: IThemeRepository) : IThemeInteractor{
    override fun setTheme(dark: Boolean) {
        repository.setTheme(dark)
    }

    override fun getTheme(consumer: IThemeInteractor.IThemeConsumer){
        consumer.consume(repository.getTheme())
    }
}