package com.practicum.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.models.PlayerScreenState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private val viewModel by viewModel<PlayerViewModel>() {
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(TRACK) as? Track
        }
        parametersOf(track)
    }

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.playButton.setOnClickListener {
            viewModel.handlePlayback()
        }

        viewModel.screenState.observe(this) {
            render(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }
    }

    private fun init(track: Track, currentTrackTime: String, playButtonState: Int, error: Boolean = false) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.track_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius))
            )
            .into(binding.cover)

        binding.collectionGroup.visibility = if (track.collectionName == null) View.GONE else View.VISIBLE

        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTime.text = currentTrackTime
        binding.trackLengthValue.text = track.getTrackLength()
        binding.trackCollectionValue.text = track.collectionName
        binding.trackReleaseDateValue.text = track.getShortReleaseDate()
        binding.trackPrimaryGenreValue.text = track.primaryGenreName
        binding.trackCountryValue.text = track.country

        binding.playButton.setImageResource(playButtonState)

        if (error) {
            binding.trackTime.text = getString(R.string.player_track_no_preview)
            return
        }
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Loading -> {}
            is PlayerScreenState.Content -> init(state.track, state.currentTrackTime, state.playButtonState)
            is PlayerScreenState.Error -> init(state.track, state.currentTrackTime, state.playButtonState, true)
        }
    }

    companion object {
        const val TRACK = "track"
    }
}