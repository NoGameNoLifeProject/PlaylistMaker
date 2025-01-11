package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.player.ui.PlayerActivity.Companion.TRACK
import com.practicum.playlistmaker.search.domain.models.SearchScreenState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.getViewModelFactory(
        application,
        Creator.getTracksInteractor(),
        Creator.getSearchHistoryInteractor())
    }

    private lateinit var binding: ActivitySearchBinding

    private var trackClickAllowed = true

    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var searchHistoryAdapter: SearchResultsAdapter

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this) {
            render(it)
        }

        binding.toolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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

        viewModel.searchHistory.observe(this) {
            searchHistoryAdapter.updateData(it)
        }

        viewModel.searchQuery.observe(this) { query ->
            if (binding.search.text.toString() != query) {
                binding.search.setText(query)
                binding.search.setSelection(query.length)
            }
        }

        viewModel.showToast.observe(this) {
            showToast(it)
        }

        binding.searchLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun openPlayer(track: Track){
        if (!isTrackClickAllowed()) return

//        val intent = Intent(this, PlayerActivity::class.java)
//        intent.putExtra(TRACK, Gson().toJson(track))
//        startActivity(intent)
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(TRACK, track)
        startActivity(intent)
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showContent(state.tracks)
            is SearchScreenState.History -> showHistory()
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

    private fun showHistory() {
        hideAll()
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
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isTrackClickAllowed() : Boolean {
        val current = trackClickAllowed
        if (trackClickAllowed) {
            trackClickAllowed = false
            mainHandler.postDelayed({ trackClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}