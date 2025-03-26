package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.domain.api.IFavoritesInteractor
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.impl.FavoritesInteractor
import com.practicum.playlistmaker.media.domain.impl.PlaylistInteractor
import com.practicum.playlistmaker.player.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractor
import com.practicum.playlistmaker.search.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ITracksInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractor
import com.practicum.playlistmaker.settings.domain.api.IThemeInteractor
import com.practicum.playlistmaker.settings.domain.impl.ThemeInteractor
import com.practicum.playlistmaker.sharing.domain.api.ISharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractor
import org.koin.dsl.module

val interactorModule = module {
    factory<IPlayerInteractor> {
        PlayerInteractor(get())
    }

    factory<ITracksInteractor> {
        TracksInteractor(get())
    }

    factory<ISearchHistoryInteractor> {
        SearchHistoryInteractor(get())
    }

    factory<IThemeInteractor> {
        ThemeInteractor(get())
    }

    factory<ISharingInteractor> {
        SharingInteractor(get(), get())
    }

    factory<IFavoritesInteractor> {
        FavoritesInteractor(get())
    }

    factory<IPlaylistInteractor> {
        PlaylistInteractor(get(), get())
    }
}