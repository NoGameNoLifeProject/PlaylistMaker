package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.domain.api.IThemeInteractor
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.api.ISharingInteractor

class SettingsViewModel(application: Application,
                        private val themeInteractor: IThemeInteractor,
                        private val sharingInteractor: ISharingInteractor) : AndroidViewModel(application) {

    private val _themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettings: LiveData<ThemeSettings>
        get() = _themeSettings

    init {
        themeInteractor.getTheme {
            _themeSettings.value = it
        }
    }

    companion object {
        fun getViewModelFactory(
            application: Application,
            sharingInteractor: ISharingInteractor,
            themeInteractor: IThemeInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(application, themeInteractor, sharingInteractor)
            }
        }
    }

    fun setTheme(isDark: Boolean) {
        themeInteractor.setTheme(ThemeSettings(isDark))
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeToSupport() {
        sharingInteractor.openSupport()
    }

    fun termsOfUse() {
        sharingInteractor.openTerms()
    }
}