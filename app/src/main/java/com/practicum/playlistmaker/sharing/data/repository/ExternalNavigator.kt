package com.practicum.playlistmaker.sharing.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.domain.models.EmailData
import com.practicum.playlistmaker.sharing.domain.repository.IExternalNavigator

class ExternalNavigator(val context: Context) : IExternalNavigator {
    override fun openLink(termsLink: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(termsLink)
        }
        val termsOfUseIntent = Intent.createChooser(sendIntent, null)
        termsOfUseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(termsOfUseIntent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.email))
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        }
        val openEmailIntent = Intent.createChooser(sendIntent, null)
        openEmailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(openEmailIntent)
    }

    override fun shareLink(shareAppLink: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
        }
        val shareAppIntent = Intent.createChooser(sendIntent, null)
        shareAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareAppIntent)
    }
}