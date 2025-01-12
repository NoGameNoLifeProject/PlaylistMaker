package com.practicum.playlistmaker.search.domain.models

sealed interface IResource<T> {
    data class Success<T>(val data: T) : IResource<T>
    data class Error<T>(val message: String, val code: Int? = null) : IResource<T>
    data class NetworkError<T>(val message: String) : IResource<T>
}