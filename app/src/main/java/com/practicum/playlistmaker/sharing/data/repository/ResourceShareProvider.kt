package com.practicum.playlistmaker.sharing.data.repository

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.repository.IResourceShareProvider

class ResourceShareProvider(private val context: Context) : IResourceShareProvider {
    override fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.settings_support_email),
            subject = context.getString(R.string.settings_support_subject),
            text = context.getString(R.string.settings_support_message)
        )
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.settings_user_agreement_link)
    }

    override fun getShareAppLink(): String {
        return context.getString(R.string.settings_share_link)
    }
}
