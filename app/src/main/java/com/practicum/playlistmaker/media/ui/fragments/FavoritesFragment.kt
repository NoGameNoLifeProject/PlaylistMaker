package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.domain.models.FavoritesScreenState
import com.practicum.playlistmaker.media.ui.view_models.FavoritesViewModel
import com.practicum.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {
    private val viewModel by viewModel<FavoritesViewModel>()

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            FavoritesScreenState.Content -> showContent()
            FavoritesScreenState.Error -> showError()
        }
    }

    private fun hideAll() {
        binding.favoritesResultsErrorsImage.isVisible = false
        binding.favoritesResultsErrorsText.isVisible = false
    }

    private fun showError() {
        hideAll()
        binding.favoritesResultsErrorsImage.isVisible = true
        binding.favoritesResultsErrorsText.isVisible = true
    }

    private fun showContent() {
        hideAll()
        //TODO: show content
    }
}