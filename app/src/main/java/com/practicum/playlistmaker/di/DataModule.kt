package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.INetworkClient
import com.practicum.playlistmaker.search.data.network.IApiServiceItunes
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.utils.Const.PREFERENCES_PLAY_LIST_MAKER
import com.practicum.playlistmaker.utils.Const.iTunesBaseUrl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<IApiServiceItunes> {
        Retrofit.Builder().baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(IApiServiceItunes::class.java)
    }

    single<INetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        androidContext().getSharedPreferences(PREFERENCES_PLAY_LIST_MAKER, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }
}