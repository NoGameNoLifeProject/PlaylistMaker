package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.view_models.CreatePlaylistViewModel
import com.practicum.playlistmaker.media.ui.view_models.EditPlaylistViewModel
import com.practicum.playlistmaker.media.ui.view_models.FavoritesViewModel
import com.practicum.playlistmaker.media.ui.view_models.PlaylistViewModel
import com.practicum.playlistmaker.media.ui.view_models.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel { (playlistId: Long) ->
        EditPlaylistViewModel(playlistId, get())
    }

    viewModel { (playlistId: Long) ->
        PlaylistViewModel(playlistId, get())
    }
}