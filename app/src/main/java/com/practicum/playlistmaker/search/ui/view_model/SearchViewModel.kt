package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.search.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ITracksInteractor
import com.practicum.playlistmaker.search.domain.models.ISearchResult
import com.practicum.playlistmaker.search.domain.models.SearchScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent

class SearchViewModel(
    private val searchTracksInteractor: ITracksInteractor,
    private val searchHistoryInteractor: ISearchHistoryInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val _state = MutableLiveData<SearchScreenState>()
    private val _searchQuery = MutableLiveData("")
    private val _showToast = SingleLiveEvent<String>()
    private val _searchHistory: MutableList<Track> = mutableListOf()
    private var latestSearchText: String? = null

    val state: LiveData<SearchScreenState>
        get() = _state

    val searchQuery: LiveData<String>
        get() = _searchQuery

    val showToast: LiveData<String>
        get() = _showToast

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return
        if (changedText == ""){
            clearSearchQuery()
            return
        }

        latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(query: String) {
        if (query.isEmpty()) return
        renderState(SearchScreenState.Loading)

        searchTracksInteractor.searchTracks(query) {
            when (it) {
                is ISearchResult.Data -> {
                    if (it.data.isEmpty())
                        renderState(SearchScreenState.Empty)
                    else
                        renderState(SearchScreenState.Content(it.data))
                }
                is ISearchResult.Error -> {
                    renderState(SearchScreenState.Error)
                    _showToast.postValue(it.message)
                }
                is ISearchResult.NetworkError -> {
                    renderState(SearchScreenState.Error)
                    _showToast.postValue(it.message)
                }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        _state.postValue(state)
    }

    private fun reloadSearchHistory() {
        searchHistoryInteractor.getSearchHistory {
            _searchHistory.clear()
            _searchHistory.addAll(it)
            if (_state.value is SearchScreenState.History)
                renderState(SearchScreenState.History(_searchHistory))
        }
    }

    fun addSearchHistory(track: Track) {
        searchHistoryInteractor.addSearchHistory(track)
        reloadSearchHistory()
    }

    fun clearSearchHistory() {
        renderState(SearchScreenState.Content(listOf()))
        searchHistoryInteractor.clearSearchHistory()
        reloadSearchHistory()
    }

    fun clearSearchQuery() {
        latestSearchText = ""
        _searchQuery.value = ""
        renderState(SearchScreenState.Content(listOf()))
        searchHistoryShow()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun searchHistoryShow() {
        reloadSearchHistory()
        if (_searchHistory.isNotEmpty())
            renderState(SearchScreenState.History(_searchHistory))
    }

    fun onSearchFocus() {
        if (_searchQuery.value != "") return

        searchHistoryShow()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(
            searchTracksInteractor: ITracksInteractor,
            searchHistoryInteractor: ISearchHistoryInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(searchTracksInteractor, searchHistoryInteractor)
            }
        }
    }
}