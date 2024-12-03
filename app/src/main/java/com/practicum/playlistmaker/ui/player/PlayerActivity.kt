package com.practicum.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.domain.models.EPlayerState
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track

    private lateinit var toolBar: MaterialToolbar
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

    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var trackTimeRunnable: Runnable

    private val playerInteractor = Creator.getPlayerInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        toolBar = findViewById(R.id.toolBar)
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

        toolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        init()

        playButton.setOnClickListener {
            playbackControl()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.track_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius))
            )
            .into(trackCover)

        collectionGroup.visibility = if (track.collectionName == null) View.GONE else View.VISIBLE

        trackName.text = track.trackName
        trackArtistName.text = track.artistName
        trackTime.text = getString(R.string.player_track_time_default)
        trackLength.text = track.getTrackLength()
        trackCollection.text = track.collectionName
        trackReleaseDate.text = track.getShortReleaseDate()
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        if (track.previewUrl.isNullOrEmpty()) {
            trackTime.text = getString(R.string.player_track_no_preview)
            return
        }

        playerInteractor.setOnErrorListener {
            trackTime.text = getString(R.string.player_track_no_preview)
        }
        playerInteractor.setOnPreparedListener {
            playButton.isClickable = true
        }
        playerInteractor.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_icon)
            stopTrackTimeRunnable()
            trackTime.text = getString(R.string.player_track_time_default)
        }
        playerInteractor.prepare(track.previewUrl!!)
    }

    private fun createTrackTimeRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                trackTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(playerInteractor.getCurrentPosition())

                if (playerInteractor.getState() == EPlayerState.PLAYING)
                    mainHandler.postDelayed(this, TRACK_TIME_UPDATE_DELAY)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTrackTimeRunnable()
        playerInteractor.release()
    }

    private fun playbackControl() {
        when (playerInteractor.getState()) {
            EPlayerState.PLAYING -> {
                pausePlayer()
            }

            EPlayerState.PREPARED, EPlayerState.PAUSED -> {
                startPlayer()
            }

            EPlayerState.DEFAULT -> return
        }
    }

    private fun startPlayer() {
        playerInteractor.play()
        playButton.setImageResource(R.drawable.pause_icon)

        startTrackTimeRunnable()
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        playButton.setImageResource(R.drawable.play_icon)

        stopTrackTimeRunnable()
    }

    private fun startTrackTimeRunnable() {
        trackTimeRunnable = createTrackTimeRunnable()
        mainHandler.post(trackTimeRunnable)
    }

    private fun stopTrackTimeRunnable() {
        if (!this::trackTimeRunnable.isInitialized) return

        mainHandler.removeCallbacks(trackTimeRunnable)
    }

    companion object {
        const val TRACK = "track"
        private const val TRACK_TIME_UPDATE_DELAY = 500L
    }
}