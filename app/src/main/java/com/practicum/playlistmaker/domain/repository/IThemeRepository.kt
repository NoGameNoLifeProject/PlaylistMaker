package com.practicum.playlistmaker.domain.repository

interface IThemeRepository {
    fun setTheme(dark: Boolean)
    fun getTheme(): Boolean
}