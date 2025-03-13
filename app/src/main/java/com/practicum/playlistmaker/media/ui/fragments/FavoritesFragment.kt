package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.domain.models.FavoritesScreenState
import com.practicum.playlistmaker.media.ui.adapters.FavoritesAdapter
import com.practicum.playlistmaker.media.ui.view_models.FavoritesViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {
    private val viewModel by viewModel<FavoritesViewModel>()

    private lateinit var adapter: FavoritesAdapter

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoritesAdapter {
            val action = MediaFragmentDirections.actionMediaFragmentToPlayerFragment(it)
            findNavController().navigate(action)
        }
        binding.rvFavorites.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.loadFavorites()
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Content -> showContent(state.tracks)
            is FavoritesScreenState.Empty -> showEmpty()
            is FavoritesScreenState.Loading -> showLoading()
        }
    }

    private fun hideAll() {
        binding.favoritesResultsEmpty.isVisible = false
        binding.progressBar.isVisible = false
        binding.rvFavorites.isVisible = false
    }

    private fun showLoading() {
        hideAll()
        binding.progressBar.isVisible = true
    }

    private fun showEmpty() {
        hideAll()
        binding.favoritesResultsEmpty.isVisible = true
    }

    private fun showContent(tracks: List<Track>) {
        hideAll()
        adapter.setTracks(tracks)
        binding.rvFavorites.isVisible = true
    }
}