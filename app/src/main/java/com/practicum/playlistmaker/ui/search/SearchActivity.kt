package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.PlayerActivity
import com.practicum.playlistmaker.ui.player.PlayerActivity.Companion.TRACK
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.models.ESearchResultsStates

class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    private val tracks: MutableList<Track> = mutableListOf()
    private val history: MutableList<Track> = mutableListOf()
    private var trackClickAllowed = true

    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var searchHistoryAdapter: SearchResultsAdapter

    private lateinit var toolBar: MaterialToolbar
    private lateinit var searchLayout: TextInputLayout
    private lateinit var searchBar: TextInputEditText

    private lateinit var progressBar: ProgressBar

    private lateinit var rvSearchResults: RecyclerView

    private lateinit var searchResultsErrors: LinearLayout
    private lateinit var searchResultsErrorsImage: ImageView
    private lateinit var searchResultsErrorsText: TextView
    private lateinit var searchResultsErrorsUpdate: Button

    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var searchHistoryClearButton: Button

    private val mainHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }
    private var searchCallbackRunnable: Runnable? = null

    private val tracksInteractor = Creator.getTracksInteractor()
    private val searchHistoryInteractor = Creator.getSearchHistoryInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        toolBar = findViewById(R.id.toolBar)
        searchLayout = findViewById(R.id.search_layout)
        searchBar = findViewById(R.id.search)
        progressBar = findViewById(R.id.progressBar)
        rvSearchResults = findViewById(R.id.rv_search_results)
        searchResultsErrors = findViewById(R.id.search_results_errors)
        searchResultsErrorsImage = findViewById(R.id.search_results_errors_image)
        searchResultsErrorsText = findViewById(R.id.search_results_errors_text)
        searchResultsErrorsUpdate = findViewById(R.id.search_results_errors_update)
        searchHistoryLayout = findViewById(R.id.search_history)
        rvSearchHistory = findViewById(R.id.rv_search_history)
        searchHistoryClearButton = findViewById(R.id.search_history_clear_button)

        searchResultsAdapter = SearchResultsAdapter(tracks) {
            searchHistoryInteractor.addSearchHistory(it)
            searchHistoryInteractor.getSearchHistory {tracks ->
                updateHistory(tracks)
            }
            openPlayer(it)
        }
        searchHistoryAdapter = SearchResultsAdapter(history) {
            searchHistoryInteractor.addSearchHistory(it)
            searchHistoryInteractor.getSearchHistory {tracks ->
                updateHistory(tracks)
            }
            openPlayer(it)
        }

        searchHistoryInteractor.getSearchHistory {tracks ->
            updateHistory(tracks)
        }

        rvSearchResults.adapter = searchResultsAdapter
        rvSearchHistory.adapter = searchHistoryAdapter

        toolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        searchLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            searchBar.setText("")
            tracks.clear()
            searchResultsAdapter.notifyDataSetChanged()
            toggleResultsState(ESearchResultsStates.TracksList)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchHistoryLayout.isVisible = searchBar.hasFocus() && s?.isEmpty() == true && history.isNotEmpty()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        searchBar.addTextChangedListener(searchTextWatcher)

        searchResultsErrorsUpdate.setOnClickListener { search() }

        searchBar.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.isVisible = hasFocus && searchBar.text?.isEmpty() == true && history.isNotEmpty()
        }

        searchHistoryClearButton.setOnClickListener {
            searchHistoryInteractor.clearSearchHistory()
            searchHistoryLayout.isVisible = false
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateHistory(tracks: List<Track>){
        history.clear()
        history.addAll(tracks)
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun openPlayer(track: Track){
        if (!isTrackClickAllowed()) return

        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(TRACK, Gson().toJson(track))
        startActivity(intent)
    }

    private fun search() {
        if (searchText.isEmpty()) {
            tracks.clear()
            searchResultsAdapter.notifyDataSetChanged()
            return
        }

        toggleResultsState(ESearchResultsStates.Loading)

        tracksInteractor.searchTracks(searchText) {
            searchCallbackRunnable = Runnable {
                tracks.clear()
                tracks.addAll(it)
                searchResultsAdapter.notifyDataSetChanged()
                toggleResultsState(if (tracks.isEmpty()) ESearchResultsStates.NoResults else ESearchResultsStates.TracksList)
            }
            mainHandler.post(searchCallbackRunnable!!)
        }
    }

    private fun searchDebounce() {
        mainHandler.removeCallbacks(searchRunnable)
        mainHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun toggleResultsState(state: ESearchResultsStates) {
        when (state) {
            ESearchResultsStates.Loading -> {
                rvSearchResults.visibility = View.GONE
                searchResultsErrors.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            ESearchResultsStates.TracksList -> {
                progressBar.visibility = View.GONE
                searchResultsErrors.visibility = View.GONE
                rvSearchResults.visibility = View.VISIBLE
            }
            ESearchResultsStates.NoResults -> {
                rvSearchHistory.visibility = View.GONE
                progressBar.visibility = View.GONE
                searchResultsErrors.visibility = View.VISIBLE
                searchResultsErrorsImage.setImageResource(R.drawable.no_results_icon)
                searchResultsErrorsText.text = getString(R.string.search_no_results)
                searchResultsErrorsUpdate.visibility = View.GONE
            }
            ESearchResultsStates.NoInternet -> {
                rvSearchHistory.visibility = View.GONE
                progressBar.visibility = View.GONE
                searchResultsErrors.visibility = View.VISIBLE
                searchResultsErrorsImage.setImageResource(R.drawable.no_internet_icon)
                searchResultsErrorsText.text = getString(R.string.search_no_internet)
                searchResultsErrorsUpdate.visibility = View.VISIBLE
            }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, "")
        val searchBar = findViewById<EditText>(R.id.search)
        searchBar.setText(searchText)
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}