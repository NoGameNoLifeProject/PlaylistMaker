package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Creator.initApplication(this)

        val themeInteractor = Creator.getThemeInteractor()
        themeInteractor.getTheme() {
            themeInteractor.setTheme(it)
        }
    }
}