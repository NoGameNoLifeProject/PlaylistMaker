package com.practicum.playlistmaker.domain.repository

interface ISettingsRepository {
    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
}