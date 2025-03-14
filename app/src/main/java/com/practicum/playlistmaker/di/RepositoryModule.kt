package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.repository.PlayerRepository
import com.practicum.playlistmaker.player.domain.repository.IPlayerRepository
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepository
import com.practicum.playlistmaker.search.data.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.repository.ISearchHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.ITracksRepository
import com.practicum.playlistmaker.settings.data.repository.ThemeRepository
import com.practicum.playlistmaker.settings.domain.repository.IThemeRepository
import com.practicum.playlistmaker.sharing.data.repository.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.repository.ResourceShareProvider
import com.practicum.playlistmaker.sharing.domain.repository.IExternalNavigator
import com.practicum.playlistmaker.sharing.domain.repository.IResourceShareProvider
import org.koin.dsl.module

val repositoryModule = module {
    factory<IPlayerRepository> {
        PlayerRepository(get())
    }

    single<ISearchHistoryRepository> {
        SearchHistoryRepository(get(), get())
    }

    single<ITracksRepository> {
        TracksRepository(get())
    }

    single<IThemeRepository> {
        ThemeRepository(get(), get())
    }

    single<IExternalNavigator> {
        ExternalNavigator(get())
    }

    single<IResourceShareProvider> {
        ResourceShareProvider(get())
    }
}