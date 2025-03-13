package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.media.data.converters.TrackDbConverter
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.ISearchNetworkClient
import com.practicum.playlistmaker.search.data.network.IApiServiceItunes
import com.practicum.playlistmaker.search.data.network.RetrofitSearchNetworkClient
import com.practicum.playlistmaker.utils.Const.DATABASE_NAME
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

    single<ISearchNetworkClient> {
        RetrofitSearchNetworkClient(get())
    }

    single {
        androidContext().getSharedPreferences(PREFERENCES_PLAY_LIST_MAKER, Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_NAME).build()
    }

    factory { Gson() }

    factory { MediaPlayer() }

    factory { TrackDbConverter() }
}