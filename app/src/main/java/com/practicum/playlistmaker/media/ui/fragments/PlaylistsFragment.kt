package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.domain.models.PlaylistsScreenState
import com.practicum.playlistmaker.media.ui.view_models.PlaylistsViewModel
import com.practicum.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment  : BindingFragment<FragmentPlaylistsBinding>() {
    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.playlistsAddButton.setOnClickListener {
            //TODO: add playlist
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            PlaylistsScreenState.Content -> showContent()
            PlaylistsScreenState.Error -> showError()
        }
    }

    private fun hideAll() {
        binding.playlistsResultsErrorsImage.isVisible = false
        binding.playlistsResultsErrorsText.isVisible = false
    }

    private fun showError() {
        hideAll()
        binding.playlistsResultsErrorsImage.isVisible = true
        binding.playlistsResultsErrorsText.isVisible = true
    }

    private fun showContent() {
        hideAll()
        //TODO: show content
    }
}