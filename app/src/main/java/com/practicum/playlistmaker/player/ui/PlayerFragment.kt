package com.practicum.playlistmaker.player.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.domain.models.PlayerScreenState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.BindingFragment
import com.practicum.playlistmaker.utils.TextUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {
    private val args: PlayerFragmentArgs by navArgs()
    private val viewModel: PlayerViewModel by viewModel { parametersOf(args.track) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistAdapter: PlaylistsAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backPlayerImage.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            viewModel.handlePlayback()
        }

        viewModel.screenState.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.addFavoritesButton.setOnClickListener {
            viewModel.handleFavorite()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistAddBottomSheet)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.addPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        playlistAdapter = PlaylistsAdapter({ count ->
            TextUtils.getTracksCountEnding(requireContext(), count)
        }, { viewModel.addToPlaylist(it) })
        binding.rvPlaylists.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.setPlaylists(playlists)
        }

        binding.btnCreatePlaylist.setOnClickListener {
            val action = PlayerFragmentDirections.actionPlayerFragmentToCreatePlaylistFragment()
            findNavController().navigate(action)
        }

        viewModel.showToast.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, getString(it.first, it.second), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(
                    MaterialColors.getColor(requireContext(), R.attr.snackBarBackgroundColor, Color.BLACK)
                )
                .setTextColor(
                    MaterialColors.getColor(requireContext(), R.attr.snackBarTextColor, Color.WHITE)
                )
                .show()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun init(
        track: Track,
        currentTrackTime: String,
        playButtonState: Int,
        error: Boolean = false
    ) {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.track_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius))
            )
            .into(binding.cover)

        binding.collectionGroup.visibility =
            if (track.collectionName == null) View.GONE else View.VISIBLE

        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTime.text = currentTrackTime
        binding.trackLengthValue.text = track.getTrackLength()
        binding.trackCollectionValue.text = track.collectionName
        binding.trackReleaseDateValue.text = track.getShortReleaseDate()
        binding.trackPrimaryGenreValue.text = track.primaryGenreName
        binding.trackCountryValue.text = track.country

        binding.playButton.setImageResource(playButtonState)

        if (track.isFavorite)
            binding.addFavoritesButton.setImageResource(R.drawable.favourites_icon_active)
        else
            binding.addFavoritesButton.setImageResource(R.drawable.favourites_icon)

        if (error) {
            binding.trackTime.text = getString(R.string.player_track_no_preview)
            return
        }
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Loading -> {}
            is PlayerScreenState.Content -> init(
                state.track,
                state.currentTrackTime,
                state.playButtonState
            )

            is PlayerScreenState.Error -> init(
                state.track,
                state.currentTrackTime,
                state.playButtonState,
                true
            )
        }
    }
}