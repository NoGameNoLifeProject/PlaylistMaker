package com.practicum.playlistmaker.data.dto

data class TrackDto(
    val trackId: Long, //Уникальный идентификатор
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String, // Год релиза
    val primaryGenreName: String, // Жанр
    val country: String, //Страна исполнителя
    val previewUrl: String? //Url для воспроизведения
)
