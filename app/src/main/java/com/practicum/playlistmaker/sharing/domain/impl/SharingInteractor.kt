package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.ISharingInteractor
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.repository.IExternalNavigator
import com.practicum.playlistmaker.sharing.domain.repository.IResourceShareProvider

class SharingInteractor(
    private val externalNavigator: IExternalNavigator,
    private val resourceShareProvider: IResourceShareProvider
) : ISharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return resourceShareProvider.getShareAppLink()
    }

    private fun getTermsLink(): String {
        return resourceShareProvider.getTermsLink()
    }

    private fun getSupportEmailData(): EmailData {
        return resourceShareProvider.getSupportEmailData()
    }
}