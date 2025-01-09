package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.repository.PlayerRepository
import com.practicum.playlistmaker.data.repository.SearchHistoryRepository
import com.practicum.playlistmaker.data.repository.ThemeRepository
import com.practicum.playlistmaker.data.repository.TracksRepository
import com.practicum.playlistmaker.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.IThemeInteractor
import com.practicum.playlistmaker.domain.api.ITracksInteractor
import com.practicum.playlistmaker.domain.impl.PlayerInteractor
import com.practicum.playlistmaker.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.impl.ThemeInteractor
import com.practicum.playlistmaker.domain.impl.TracksInteractor
import com.practicum.playlistmaker.domain.repository.IPlayerRepository
import com.practicum.playlistmaker.domain.repository.ISearchHistoryRepository
import com.practicum.playlistmaker.domain.repository.IThemeRepository
import com.practicum.playlistmaker.domain.repository.ITracksRepository

object Creator {
    private lateinit var applications: Application

    fun initApplication(application: Application) {
        this.applications = application
    }

    private fun provideTracksRepository(): ITracksRepository {
        return TracksRepository(RetrofitNetworkClient())
    }

    private fun provideSearchHistoryRepository(): ISearchHistoryRepository {
        return SearchHistoryRepository(applications)
    }

    private fun providePlayerRepository(): IPlayerRepository {
        return PlayerRepository()
    }

    private fun provideThemeRepository(): IThemeRepository {
        return ThemeRepository(applications)
    }

    fun getTracksInteractor(): ITracksInteractor {
        return TracksInteractor(provideTracksRepository())
    }

    fun getSearchHistoryInteractor(): ISearchHistoryInteractor {
        return SearchHistoryInteractor(provideSearchHistoryRepository())
    }

    fun getPlayerInteractor(): IPlayerInteractor {
        return PlayerInteractor(providePlayerRepository())
    }

    fun getThemeInteractor(): IThemeInteractor {
        return ThemeInteractor(provideThemeRepository())
    }
}