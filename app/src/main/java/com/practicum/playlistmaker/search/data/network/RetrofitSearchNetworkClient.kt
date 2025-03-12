package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.ISearchNetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitSearchNetworkClient(private val iTunesService: IApiServiceItunes) : ISearchNetworkClient {
    override suspend fun searchTracks(query: String): Response {
        return withContext(Dispatchers.IO) {
            //TODO: Добавить проверку ошибки сети
            try {
                val response = iTunesService.search(query)
                response.apply { resultCode = SUCCESS_CODE }
            } catch (e: Throwable) {
                Response().apply {
                    resultCode = ERROR_CODE
                    message = "При выполнении запроса произошла ошибка: ${e.message}"
                }
            }
        }
    }

    companion object {
        const val SUCCESS_CODE = 200
        const val ERROR_CODE = 500
    }
}