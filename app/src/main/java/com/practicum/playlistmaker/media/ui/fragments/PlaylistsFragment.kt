package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.media.domain.models.PlaylistsScreenState
import com.practicum.playlistmaker.media.ui.adapters.PlaylistsAdapter
import com.practicum.playlistmaker.media.ui.view_models.PlaylistsViewModel
import com.practicum.playlistmaker.utils.BindingFragment
import com.practicum.playlistmaker.utils.TextUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {
    private val viewModel by viewModel<PlaylistsViewModel>()
    private lateinit var adapter: PlaylistsAdapter

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistsAdapter({ count ->
            TextUtils.getTracksCountEnding(requireContext(), count)
        }, {
            // TODO: Open playlist
        })

        binding.rvPlaylists.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@PlaylistsFragment.adapter
        }

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.playlistsAddButton.setOnClickListener {
            val action = MediaFragmentDirections.actionMediaFragmentToCreatePlaylistFragment()
            findNavController().navigate(action)
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Content -> showContent(state.playlists)
            is PlaylistsScreenState.Empty -> showError()
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

    private fun showContent(playlists: List<PlaylistWithTracks>) {
        hideAll()
        adapter.setPlaylists(playlists)
    }
}