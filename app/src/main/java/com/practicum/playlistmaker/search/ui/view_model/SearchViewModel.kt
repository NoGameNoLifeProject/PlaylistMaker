package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.ISearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ITracksInteractor
import com.practicum.playlistmaker.search.domain.models.ISearchResult
import com.practicum.playlistmaker.search.domain.models.SearchScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksInteractor: ITracksInteractor,
    private val searchHistoryInteractor: ISearchHistoryInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchScreenState>()
    private val _searchQuery = MutableLiveData("")
    private val _showToast = SingleLiveEvent<String>()
    private val _searchHistory: MutableList<Track> = mutableListOf()

    val state: LiveData<SearchScreenState>
        get() = _state

    val searchQuery: LiveData<String>
        get() = _searchQuery

    val showToast: LiveData<String>
        get() = _showToast

    val searchDebounce =
        debounce(SEARCH_DEBOUNCE_DELAY, viewModelScope, true, this::onSearchDebounce)

    fun onSearchDebounce(changedText: String) {
        if (_searchQuery.value == changedText) return
        if (changedText == "") {
            clearSearchQuery()
            return
        }

        _searchQuery.value = changedText
        searchRequest(changedText)
    }

    private fun searchRequest(query: String) {
        if (query.isEmpty()) return
        renderState(SearchScreenState.Loading)

        viewModelScope.launch {
            searchTracksInteractor.searchTracks(query).collect {
                when (it) {
                    is ISearchResult.Data -> {
                        if (it.data.isEmpty())
                            renderState(SearchScreenState.Empty)
                        else
                            renderState(SearchScreenState.Content(it.data))
                    }

                    is ISearchResult.Error -> {
                        renderState(SearchScreenState.Error)
                        _showToast.value = it.message
                    }

                    is ISearchResult.NetworkError -> {
                        renderState(SearchScreenState.Error)
                        _showToast.value = it.message
                    }
                }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        _state.value = state
    }

    private suspend fun reloadSearchHistory() {
        val history = searchHistoryInteractor.getSearchHistory()
        _searchHistory.clear()
        _searchHistory.addAll(history)
        if (_state.value is SearchScreenState.History)
            renderState(SearchScreenState.History(_searchHistory))
    }

    fun addSearchHistory(track: Track) {
        viewModelScope.launch {
            searchHistoryInteractor.addSearchHistory(track)
            reloadSearchHistory()
        }
    }

    fun clearSearchHistory() {
        renderState(SearchScreenState.Content(listOf()))
        searchHistoryInteractor.clearSearchHistory()
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
        renderState(SearchScreenState.Content(listOf()))
        searchHistoryShow()
    }

    private fun searchHistoryShow() {
        viewModelScope.launch {
            reloadSearchHistory()
            if (_searchHistory.isNotEmpty())
                renderState(SearchScreenState.History(_searchHistory))
        }
    }

    fun onSearchFocus() {
        if (_searchQuery.value != "") return

        searchHistoryShow()
    }

    fun onResume() {
        viewModelScope.launch {
            reloadSearchHistory()
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }
}