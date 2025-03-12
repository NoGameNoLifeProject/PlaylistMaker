package com.practicum.playlistmaker.search.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.models.SearchScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    private val viewModel by viewModel<SearchViewModel>()

    private var trackClickAllowed = true

    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var searchHistoryAdapter: SearchResultsAdapter

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        searchResultsAdapter = SearchResultsAdapter {
            viewModel.addSearchHistory(it)
            openPlayer(it)
        }
        binding.rvSearchResults.adapter = searchResultsAdapter

        searchHistoryAdapter = SearchResultsAdapter {
            viewModel.addSearchHistory(it)
            openPlayer(it)
        }
        binding.rvSearchHistory.adapter = searchHistoryAdapter

        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            if (binding.search.text.toString() != query) {
                binding.search.setText(query)
                binding.search.setSelection(query.length)
            }
        }

        viewModel.showToast.observe(viewLifecycleOwner) {
            showToast(it)
        }

        binding.searchLayout.setEndIconOnClickListener {
            val inputMethodManager =
                activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            binding.search.setText("")
            viewModel.clearSearchQuery()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.search.addTextChangedListener(searchTextWatcher)

        binding.searchResultsErrorsUpdate.setOnClickListener { viewModel.searchDebounce(binding.search.text.toString()) }

        binding.search.setOnFocusChangeListener { view, hasFocus ->
            viewModel.onSearchFocus()
        }

        binding.searchHistoryClearButton.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    private fun openPlayer(track: Track) {
        if (!isTrackClickAllowed()) return

        val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showContent(state.tracks)
            is SearchScreenState.History -> showHistory(state.tracks)
            is SearchScreenState.Error -> showError()
            is SearchScreenState.Empty -> showEmpty()
        }
    }

    private fun hideAll() {
        binding.rvSearchResults.visibility = View.GONE
        binding.searchHistory.visibility = View.GONE
        binding.searchResultsErrors.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        hideAll()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        hideAll()
        binding.searchResultsErrors.visibility = View.VISIBLE
        binding.searchResultsErrorsImage.setImageResource(R.drawable.no_internet_icon)
        binding.searchResultsErrorsText.text = getString(R.string.search_no_internet)
        binding.searchResultsErrorsUpdate.visibility = View.VISIBLE
    }

    private fun showContent(tracks: List<Track>) {
        hideAll()
        searchResultsAdapter.updateData(tracks)
        binding.rvSearchResults.visibility = View.VISIBLE
    }

    private fun showHistory(tracks: List<Track>) {
        hideAll()
        searchHistoryAdapter.updateData(tracks)
        binding.searchHistory.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        hideAll()
        binding.searchResultsErrorsImage.setImageResource(R.drawable.no_results_icon)
        binding.searchResultsErrorsText.text = getString(R.string.search_no_results)
        binding.searchResultsErrorsUpdate.visibility = View.GONE
    }

    private fun showToast(text: String) {
        if (text.isNotEmpty()) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isTrackClickAllowed(): Boolean {
        val current = trackClickAllowed
        if (trackClickAllowed) {
            trackClickAllowed = false
            mainHandler.postDelayed({ trackClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}