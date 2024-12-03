package com.practicum.playlistmaker.domain.api

interface IThemeInteractor {
    fun setTheme(dark: Boolean)
    fun getTheme(consumer: IThemeConsumer)

    fun interface IThemeConsumer {
        fun consume(value: Boolean)
    }
}