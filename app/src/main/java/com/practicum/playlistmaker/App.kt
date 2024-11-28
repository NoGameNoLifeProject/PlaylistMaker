package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
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