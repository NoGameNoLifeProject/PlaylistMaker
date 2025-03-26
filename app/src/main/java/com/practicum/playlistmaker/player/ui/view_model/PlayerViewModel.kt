package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.api.IFavoritesInteractor
import com.practicum.playlistmaker.media.domain.api.IPlaylistInteractor
import com.practicum.playlistmaker.media.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.player.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.EPlayerState
import com.practicum.playlistmaker.player.domain.models.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: IPlayerInteractor,
    private val favoritesInteractor: IFavoritesInteractor,
    private val playlistInteractor: IPlaylistInteractor
) : ViewModel() {

    private var _playerState = EPlayerState.DEFAULT
    private val _screenState = MutableLiveData<PlayerScreenState>()
    private val _playlists = MutableLiveData<List<PlaylistWithTracks>>(emptyList())
    private val _showToast = SingleLiveEvent<Pair<Int, String>>()
    private var _currentTrackTime = "00:00"
    private var _playButtonState = R.drawable.play_icon
    private var trackTimeJob: Job? = null

    private val dataFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }
    val screenState: LiveData<PlayerScreenState> get() = _screenState
    val playlists: LiveData<List<PlaylistWithTracks>> get() = _playlists
    val showToast: LiveData<Pair<Int, String>> get() = _showToast

    init {
        setupPlayer()

        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { playlists -> _playlists.postValue(playlists) }
        }
    }

    private fun setupPlayer() {
        if (track.previewUrl.isNullOrEmpty()) {
            _screenState.value = PlayerScreenState.Error(track, _currentTrackTime, _playButtonState)
            return
        }
        _screenState.value = PlayerScreenState.Content(track, _currentTrackTime, _playButtonState)

        playerInteractor.setOnStateChangeListener {
            _playerState = it
            updateUI()
        }
        playerInteractor.prepare(track.previewUrl)
    }

    private fun renderState(state: PlayerScreenState) {
        _screenState.value = state
    }

    private fun updateUI() {
        when (_playerState) {
            EPlayerState.DEFAULT -> {}
            EPlayerState.ERROR -> {
                renderState(PlayerScreenState.Error(track, _currentTrackTime, _playButtonState))
            }

            EPlayerState.PREPARED, EPlayerState.PAUSED -> {
                _playButtonState = R.drawable.play_icon
                if (_playerState == EPlayerState.PREPARED) {
                    _currentTrackTime = "00:00"
                    stopTrackTimer()
                }
                renderState(PlayerScreenState.Content(track, _currentTrackTime, _playButtonState))
            }

            EPlayerState.PLAYING -> {
                _playButtonState = R.drawable.pause_icon
                renderState(PlayerScreenState.Content(track, _currentTrackTime, _playButtonState))
            }
        }
    }

    fun handlePlayback() {
        when (_playerState) {
            EPlayerState.DEFAULT, EPlayerState.ERROR -> {}
            EPlayerState.PREPARED, EPlayerState.PAUSED -> {
                play()
            }

            EPlayerState.PLAYING -> {
                pause()
            }
        }
    }

    fun play() {
        playerInteractor.play()
        startTrackTimer()
    }

    fun pause() {
        playerInteractor.pause()
        stopTrackTimer()
    }

    private fun startTrackTimer() {
        trackTimeJob = viewModelScope.launch {
            while (_playerState == EPlayerState.PLAYING) {
                delay(TRACK_TIME_UPDATE_DELAY)
                _currentTrackTime = dataFormat.format(playerInteractor.getCurrentPosition())
                updateUI()
            }
        }
    }

    private fun stopTrackTimer() {
        trackTimeJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun handleFavorite() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoritesInteractor.removeFavoriteTrack(track.trackId)
            } else {
                favoritesInteractor.addFavoriteTrack(track)
            }
            track.isFavorite = !track.isFavorite
            renderState(PlayerScreenState.Content(track, _currentTrackTime, _playButtonState))
        }
    }

    fun addToPlaylist(playlist: PlaylistWithTracks) {
        viewModelScope.launch {
            if (playlist.tracks.find { it.trackId == track.trackId } != null) {
                _showToast.value =
                    Pair(R.string.player_track_already_in_playlist, playlist.playlist.name)
                return@launch
            }
            playlistInteractor.addTrackToPlaylist(playlist.playlist.playlistId, track)
            _showToast.value = Pair(R.string.player_track_added_to_playlist, playlist.playlist.name)
        }
    }

    companion object {
        private const val TRACK_TIME_UPDATE_DELAY = 500L
    }
}