package com.practicum.playlistmaker.sharing.domain.repository

import com.practicum.playlistmaker.sharing.domain.models.EmailData

interface IExternalNavigator {
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
    fun shareLink(shareAppLink: String)
}