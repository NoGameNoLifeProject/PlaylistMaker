package com.practicum.playlistmaker.media.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.media.domain.models.CreatePlaylistScreenState
import com.practicum.playlistmaker.media.ui.view_models.CreatePlaylistViewModel
import com.practicum.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : BindingFragment<FragmentCreatePlaylistBinding>() {
    private val viewModel by viewModel<CreatePlaylistViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreatePlaylistBinding {
        return FragmentCreatePlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistNameTextLayout.editText?.addTextChangedListener { text ->
            viewModel.onNameChanged(
                text.toString()
            )
        }
        binding.playlistDescTextLayout.editText?.addTextChangedListener { text ->
            viewModel.onDescriptionChanged(
                text.toString()
            )
        }
        binding.buttonCreate.setOnClickListener { viewModel.onCreateHandler() }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.coverImageView.apply {
                        setImageURI(uri)
                        setBackgroundResource(0)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    binding.coverImageView.layoutParams.let {
                        it.width = ViewGroup.LayoutParams.MATCH_PARENT
                        it.height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    binding.coverImageView.requestLayout()
                    viewModel.setImageUri(uri)
                }
            }

        binding.coverImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreate.setOnClickListener {
            viewModel.onCreateHandler()
        }

        viewModel.canCreatePlaylist.observe(viewLifecycleOwner) {
            binding.buttonCreate.isEnabled = it
        }

        binding.toolBar.setOnClickListener { viewModel.onBackHandler() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackHandler()
        }

        viewModel.screenState.observe(viewLifecycleOwner) { screenState -> render(screenState) }
    }

    private fun render(state: CreatePlaylistScreenState) {
        when (state) {
            is CreatePlaylistScreenState.NavigateBack -> findNavController().navigateUp()
            is CreatePlaylistScreenState.ShowDialogBeforeNavigateBack -> showDialog()
            is CreatePlaylistScreenState.PlaylistScreenCreated -> showPlaylistCreated(state.playlistName)
            is CreatePlaylistScreenState.Base -> {}
        }
        viewModel.resetScreenState()
    }

    private fun showPlaylistCreated(playlistName: String) {
        val message = getString(R.string.media_create_playlist_created, playlistName)
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(
                MaterialColors.getColor(requireContext(), R.attr.snackBarBackgroundColor, Color.BLACK)
            )
            .setTextColor(
                MaterialColors.getColor(requireContext(), R.attr.snackBarTextColor, Color.WHITE)
            )
            .show()
        findNavController().navigateUp()
    }

    private fun showDialog() {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.media_create_playlist_dialog_title))
            .setMessage(getString(R.string.media_create_playlist_dialog_message))
            .setNeutralButton(getString(R.string.media_create_playlist_dialog_button_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(R.string.media_create_playlist_dialog_button_finish) { _, _ ->
                findNavController().navigateUp()
            }
        confirmDialog.show()
    }
}