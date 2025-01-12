package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.Response

interface INetworkClient {
    fun doRequest(dto: Any): Response
}