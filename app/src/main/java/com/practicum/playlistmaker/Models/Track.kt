package com.practicum.playlistmaker.Models

data class Track(
    val trackId: Long, //Уникальный идентификатор
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)

class TracksResponse(val resultCount: Int,
                     val results: List<Track>)