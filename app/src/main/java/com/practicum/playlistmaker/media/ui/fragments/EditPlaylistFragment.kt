package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.ui.view_models.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment(){

    private val args: EditPlaylistFragmentArgs by navArgs()
    override val viewModel by viewModel<EditPlaylistViewModel> { parametersOf(args.playlistId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playlist.observe(viewLifecycleOwner){ playlist ->
            binding.playlistNameTextLayout.editText?.setText(playlist.name)
            binding.playlistDescTextLayout.editText?.setText(playlist.description)

            if (playlist.cover.toString() != "null") {
                binding.coverImageView.apply {
                    setImageURI(playlist.cover)
                    setBackgroundResource(0)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                binding.coverImageView.layoutParams.let {
                    it.width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                binding.coverImageView.requestLayout()
                binding.coverImageView.setImageURI(playlist.cover)
            }
        }

        binding.toolBar.title = getString(R.string.media_edit_playlist_title)
        binding.buttonCreate.text = getString(R.string.media_edit_playlist_save_button)
    }
}