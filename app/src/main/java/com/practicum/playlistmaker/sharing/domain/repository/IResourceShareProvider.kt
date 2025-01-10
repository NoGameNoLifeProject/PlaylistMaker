package com.practicum.playlistmaker.sharing.domain.repository

import com.practicum.playlistmaker.sharing.domain.models.EmailData

interface IResourceShareProvider {
    fun getSupportEmailData() : EmailData
    fun getTermsLink() : String
    fun getShareAppLink() : String
}