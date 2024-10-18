package com.practicum.playlistmaker.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.Models.Track
import com.practicum.playlistmaker.Models.TracksResponse
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SHARED_PREFERENCES_SETTINGS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SHARED_PREFERENCES_SEARCH = "search"
class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    private val tracks: MutableList<Track> = mutableListOf()

    private lateinit var itunesApi: ItunesAPI

    private lateinit var searchResultsAdapter: SearchResultsAdapter

    private lateinit var toolBar: MaterialToolbar
    private lateinit var searchLayout: TextInputLayout
    private lateinit var searchBar: TextInputEditText

    private lateinit var rvSearchResults: RecyclerView

    private lateinit var searchResultsErrors: LinearLayout
    private lateinit var searchResultsErrorsImage: ImageView
    private lateinit var searchResultsErrorsText: TextView
    private lateinit var searchResultsErrorsUpdate: Button

    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var searchHistoryClearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.itunes_api_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        itunesApi = retrofit.create(ItunesAPI::class.java)

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_SEARCH, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)

        toolBar = findViewById(R.id.toolBar)
        searchLayout = findViewById(R.id.search_layout)
        searchBar = findViewById(R.id.search)
        rvSearchResults = findViewById(R.id.rv_search_results)
        searchResultsErrors = findViewById(R.id.search_results_errors)
        searchResultsErrorsImage = findViewById(R.id.search_results_errors_image)
        searchResultsErrorsText = findViewById(R.id.search_results_errors_text)
        searchResultsErrorsUpdate = findViewById(R.id.search_results_errors_update)
        searchHistoryLayout = findViewById(R.id.search_history)
        rvSearchHistory = findViewById(R.id.rv_search_history)
        searchHistoryClearButton = findViewById(R.id.search_history_clear_button)

        searchResultsAdapter = SearchResultsAdapter(tracks) {
            searchHistory.addTrack(it)
        }

        rvSearchResults.adapter = searchResultsAdapter
        rvSearchHistory.adapter = searchHistory.searchHistoryAdapter

        toolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        searchLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            searchBar.setText("")
            tracks.clear()
            searchResultsAdapter.notifyDataSetChanged()
            toggleResultsState(SearchResultsStates.TracksList)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchHistoryLayout.visibility = if (searchBar.hasFocus() && s?.isEmpty() == true && !searchHistory.isEmpty()) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        searchBar.addTextChangedListener(searchTextWatcher)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        searchResultsErrorsUpdate.setOnClickListener { search() }

        searchBar.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility = if (hasFocus && (searchBar.text?.isEmpty() == true && !searchHistory.isEmpty())) View.VISIBLE else View.GONE
        }

        searchHistoryClearButton.setOnClickListener {
            searchHistory.clearHistory()
            searchHistoryLayout.visibility = View.GONE
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun search() {
        if (searchText.isEmpty()) {
            tracks.clear()
            searchResultsAdapter.notifyDataSetChanged()
            return
        }

        itunesApi.search(searchText).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.isSuccessful) {
                    tracks.clear()
                    val results = response.body()?.results
                    if (results?.isNotEmpty() == true) {
                        tracks.addAll(results)
                    }
                    searchResultsAdapter.notifyDataSetChanged()
                    toggleResultsState(if (tracks.isEmpty()) SearchResultsStates.NoResults else SearchResultsStates.TracksList)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                toggleResultsState(SearchResultsStates.NoInternet)
            }
        })
    }

    private fun toggleResultsState(state: SearchResultsStates) {
        when (state) {
            SearchResultsStates.TracksList -> {
                searchResultsErrors.visibility = View.GONE
            }
            SearchResultsStates.NoResults -> {
                searchResultsErrors.visibility = View.VISIBLE
                searchResultsErrorsImage.setImageResource(R.drawable.no_results_icon)
                searchResultsErrorsText.text = getString(R.string.search_no_results)
                searchResultsErrorsUpdate.visibility = View.GONE
            }
            SearchResultsStates.NoInternet -> {
                searchResultsErrors.visibility = View.VISIBLE
                searchResultsErrorsImage.setImageResource(R.drawable.no_internet_icon)
                searchResultsErrorsText.text = getString(R.string.search_no_internet)
                searchResultsErrorsUpdate.visibility = View.VISIBLE
            }
        }
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
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    enum class SearchResultsStates {
        TracksList, NoResults, NoInternet
    }
}