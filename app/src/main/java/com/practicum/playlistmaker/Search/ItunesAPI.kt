package com.practicum.playlistmaker.Search

import com.practicum.playlistmaker.Models.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}

class TracksResponse(val resultCount: Int,
                     val results: List<Track>)