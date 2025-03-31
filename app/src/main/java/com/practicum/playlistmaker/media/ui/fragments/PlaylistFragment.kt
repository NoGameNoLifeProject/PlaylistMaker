package com.practicum.playlistmaker.media.ui.fragments

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.media.ui.adapters.PlaylistTracksAdapter
import com.practicum.playlistmaker.media.ui.view_models.PlaylistViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.BindingFragment
import com.practicum.playlistmaker.utils.TextUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {
    private val args: PlaylistFragmentArgs by navArgs()
    private val viewModel by viewModel<PlaylistViewModel> { parametersOf(args.playlistId) }
    private lateinit var tracksAdapter: PlaylistTracksAdapter
    private lateinit var tracksListBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistEditBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setOnClickListener { findNavController().navigateUp() }

        initTracksBottomSheet()
        playlistEditBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistEditBottomSheet)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }

        playlistEditBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        tracksAdapter = PlaylistTracksAdapter({ track ->
            val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlayerFragment(track)
            findNavController().navigate(action)
        }, { track ->
            showDialogDeleteTrack(track)
        })

        viewModel.playlistWithTracks.observe(viewLifecycleOwner) {
            renderPlaylist(it)
            binding.emptyPlaylistText.isVisible = it.tracks.isEmpty()
            tracksAdapter.setTracks(it.tracks)
        }

        binding.rvTracksList.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            adapter = tracksAdapter
        }

        viewModel.showEmptyMessage.observe(viewLifecycleOwner) {
            Snackbar.make(
                binding.root,
                getString(R.string.media_playlist_share_empty_message),
                Snackbar.LENGTH_SHORT
            )
                .setBackgroundTint(
                    MaterialColors.getColor(
                        requireContext(),
                        R.attr.snackBarBackgroundColor,
                        Color.BLACK
                    )
                )
                .setTextColor(
                    MaterialColors.getColor(requireContext(), R.attr.snackBarTextColor, Color.WHITE)
                )
                .show()
        }

        binding.buttonShare.setOnClickListener {
            viewModel.share()
        }
        binding.buttonEdit.setOnClickListener {
            binding.overlay.isVisible = true
            playlistEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.hidePlaylistEditBottomSheet.observe(viewLifecycleOwner) {
            binding.overlay.isVisible = false
            playlistEditBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menuBottomSheetSharePlaylist.setOnClickListener { viewModel.share() }
        binding.menuBottomSheetEditPlaylist.setOnClickListener {
            val action =
                PlaylistFragmentDirections.actionPlaylistFragmentToEditPlaylistFragment(args.playlistId)
            findNavController().navigate(action)
        }
        binding.menuBottomSheetDeletePlaylist.setOnClickListener { showDialogDeletePlaylist() }

        viewModel.navigateBack.observe(viewLifecycleOwner) { findNavController().navigateUp() }
    }

    private fun renderPlaylist(playlist: PlaylistWithTracks) {
        if (playlist.playlist.cover.toString() == "null") {
            binding.playlistCover.setImageResource(R.drawable.track_placeholder)
            binding.bottomSheetPlaylistCoverImageView.setImageResource(R.drawable.track_placeholder)
        } else {
            binding.playlistCover.setImageURI(playlist.playlist.cover)
            binding.bottomSheetPlaylistCoverImageView.setImageURI(playlist.playlist.cover)
        }

        binding.playlistName.text = playlist.playlist.name
        binding.bottomSheetPlaylistName.text = playlist.playlist.name

        binding.playlistDescription.text = playlist.playlist.description

        val tracksCount = TextUtils.getTracksCountEnding(requireContext(), playlist.tracks.size)
        binding.playlistTracksCount.text = "${playlist.tracks.size} ${tracksCount}"
        binding.bottomSheetTracksCount.text = "${playlist.tracks.size} ${tracksCount}"

        val tracksDurationMillis = playlist.tracks.sumOf { it.trackTimeMillis }
        val tracksDuration =
            SimpleDateFormat("mm", Locale.getDefault()).format(tracksDurationMillis).toIntOrNull()
                ?: 0
        binding.playlistTime.text =
            "${tracksDuration} ${TextUtils.getTracksMinEnding(requireContext(), tracksDuration)}"
    }

    private fun initTracksBottomSheet() {
        val tracksListBottomSheetPlaceholder = binding.tracksListBottomSheetPlaceholder
        val mainLayout = binding.main
        mainLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tracksListBottomSheetBehavior.peekHeight = tracksListBottomSheetPlaceholder.height
                mainLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        tracksListBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksListBottomSheet)
            .apply { state = BottomSheetBehavior.STATE_COLLAPSED }

    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylist()
    }

    private fun showDialogDeleteTrack(track: Track) {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.media_playlist_delete_track_dialog_title))
            .setMessage(getString(R.string.media_playlist_delete_track_dialog_desc))
            .setNegativeButton(R.string.media_playlist_delete_track_dialog_cancel) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.media_playlist_delete_track_dialog_confirm)) { dialog, which ->
                viewModel.removeTrack(track)
            }
        confirmDialog.show()
    }

    private fun showDialogDeletePlaylist() {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.media_playlist_delete_playlist_dialog_title))
            .setMessage(getString(R.string.media_playlist_delete_playlist_dialog_desc))
            .setNegativeButton(R.string.media_playlist_delete_playlist_dialog_cancel) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.media_playlist_delete_playlist_dialog_confirm)) { dialog, which ->
                viewModel.deletePlaylist()
            }
        confirmDialog.show()
    }
}