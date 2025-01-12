package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.player.data.repository.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.IPlayerRepository
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.data.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ITracksInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractor
import com.practicum.playlistmaker.search.domain.repository.ISearchHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.ITracksRepository
import com.practicum.playlistmaker.settings.data.repository.ThemeRepository
import com.practicum.playlistmaker.settings.domain.api.IThemeInteractor
import com.practicum.playlistmaker.settings.domain.impl.ThemeInteractor
import com.practicum.playlistmaker.settings.domain.repository.IThemeRepository
import com.practicum.playlistmaker.sharing.data.repository.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.repository.ResourceShareProvider
import com.practicum.playlistmaker.sharing.domain.api.ISharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.repository.IExternalNavigator
import com.practicum.playlistmaker.sharing.domain.repository.IResourceShareProvider

object Creator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    //region internal
    private fun provideTracksRepository(): ITracksRepository {
        return TracksRepository(RetrofitNetworkClient())
    }

    private fun provideSearchHistoryRepository(): ISearchHistoryRepository {
        return SearchHistoryRepository(application)
    }

    private fun providePlayerRepository(): IPlayerRepository {
        return PlayerRepository()
    }

    private fun provideThemeRepository(): IThemeRepository {
        return ThemeRepository(application)
    }
    private fun provideExternalNavigator(): IExternalNavigator {
        return ExternalNavigator(application)
    }

    private fun provideResourceShareProvider(): IResourceShareProvider {
        return ResourceShareProvider(application)
    }
    //endregion

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

    fun getSharingInteractor(): ISharingInteractor {
        return SharingInteractor(provideExternalNavigator(), provideResourceShareProvider())
    }
}