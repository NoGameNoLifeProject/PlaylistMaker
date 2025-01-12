package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.INetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import java.io.IOException

class RetrofitNetworkClient(private val iTunesService: IApiServiceItunes) : INetworkClient {
    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            try {
                val resp = iTunesService.search(dto.query).execute()

                if (resp.isSuccessful) {
                    val body = resp.body() ?: Response()
                    return body.apply { resultCode = resp.code() }
                } else {
                    return Response().apply { resultCode = resp.code() }
                }
            } catch (e: IOException) {
                return Response().apply {
                    resultCode = -1
                    message = e.message ?: "Ошибка сети"
                }
            } catch (e: Exception) {
                return Response().apply {
                    resultCode = -1
                    message = e.message ?: "Неизвестная ошибка"
                }
            }
        } else {
            return Response().apply {
                resultCode = 400
                message = "Получен недопустимый ответ"
            }
        }
    }
}