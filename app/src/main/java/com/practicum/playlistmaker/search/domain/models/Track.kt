package com.practicum.playlistmaker.search.domain.models

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
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
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getTrackLength() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    fun getShortReleaseDate() = releaseDate.take(4)
}