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
import com.practicum.playlistmaker.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var searchText: String = ""
    private val tracks: MutableList<Track> = mutableListOf()

    private lateinit var itunesApi: ItunesAPI

    private val searchResultsAdapter = SearchResultsAdapter(tracks)

    private lateinit var toolBar: MaterialToolbar
    private lateinit var searchLayout: TextInputLayout
    private lateinit var searchBar: TextInputEditText
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var searchResultsErrors: LinearLayout
    private lateinit var searchResultsErrorsImage: ImageView
    private lateinit var searchResultsErrorsText: TextView
    private lateinit var searchResultsErrorsUpdate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.itunes_api_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        itunesApi = retrofit.create(ItunesAPI::class.java)

        toolBar = findViewById(R.id.toolBar)
        searchLayout = findViewById(R.id.search_layout)
        searchBar = findViewById(R.id.search)
        rvSearchResults = findViewById(R.id.rv_search_results)
        searchResultsErrors = findViewById(R.id.search_results_errors)
        searchResultsErrorsImage = findViewById(R.id.search_results_errors_image)
        searchResultsErrorsText = findViewById(R.id.search_results_errors_text)
        searchResultsErrorsUpdate = findViewById(R.id.search_results_errors_update)

        rvSearchResults.adapter = searchResultsAdapter

        toolBar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        searchLayout.setEndIconOnClickListener {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            searchBar.setText("")
            tracks.clear()
            searchResultsAdapter.notifyDataSetChanged()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
            override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                    }
                    searchResultsAdapter.notifyDataSetChanged()
                    if (tracks.isEmpty())
                        toggleNoResults(true)
                    else
                        toggleNoResults(false)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                toggleNoInternet(true)
            }
        })
    }

    private fun toggleNoResults(state: Boolean = true) {
        searchResultsErrors.visibility = if(state) View.VISIBLE else View.GONE
        searchResultsErrorsImage.setImageResource(R.drawable.no_results_icon)
        searchResultsErrorsText.text = getString(R.string.search_no_results)
        searchResultsErrorsUpdate.visibility = View.GONE
    }

    private fun toggleNoInternet(state: Boolean = true) {
        searchResultsErrors.visibility = if(state) View.VISIBLE else View.GONE
        searchResultsErrorsImage.setImageResource(R.drawable.no_internet_icon)
        searchResultsErrorsText.text = getString(R.string.search_no_internet)
        searchResultsErrorsUpdate.visibility = View.VISIBLE
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
}