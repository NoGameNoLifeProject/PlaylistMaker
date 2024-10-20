package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.Models.Track

class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track

    private lateinit var backButton: ImageButton
    private lateinit var trackCover: ImageView
    private lateinit var trackName: TextView
    private lateinit var trackArtistName: TextView
    private lateinit var addPlayListButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var addFavoritesButton: ImageButton
    private lateinit var trackTime: TextView
    private lateinit var trackLength: TextView
    private lateinit var trackCollection: TextView
    private lateinit var trackReleaseDate: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView

    private lateinit var collectionGroup: Group
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        backButton = findViewById(R.id.backButton)
        trackCover = findViewById(R.id.cover)
        trackName = findViewById(R.id.track_name)
        trackArtistName = findViewById(R.id.track_artist)
        addPlayListButton = findViewById(R.id.addPlaylistButton)
        playButton = findViewById(R.id.playButton)
        addFavoritesButton = findViewById(R.id.addFavoritesButton)
        trackTime = findViewById(R.id.track_time)
        trackLength = findViewById(R.id.track_length_value)
        trackCollection = findViewById(R.id.track_collection_value)
        trackReleaseDate = findViewById(R.id.track_release_date_value)
        trackGenre = findViewById(R.id.track_primary_genre_value)
        trackCountry = findViewById(R.id.track_country_value)
        collectionGroup = findViewById(R.id.collectionGroup)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)
        init()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
    }

    private fun init(){
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius))
            )
            .into(trackCover)

        collectionGroup.visibility = if (track.collectionName == null) View.GONE else View.VISIBLE

        trackName.text = track.trackName
        trackArtistName.text = track.artistName
        //trackTime.text =
        trackLength.text = track.getTrackLength()
        trackCollection.text = track.collectionName
        trackReleaseDate.text = track.getShortReleaseDate()
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
    }

    companion object {
        const val TRACK = "track"
    }
}