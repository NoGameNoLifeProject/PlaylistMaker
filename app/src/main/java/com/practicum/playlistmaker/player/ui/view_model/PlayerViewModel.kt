package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.api.IPlayerInteractor
import com.practicum.playlistmaker.player.domain.models.EPlayerState
import com.practicum.playlistmaker.player.domain.models.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: IPlayerInteractor
) : ViewModel() {

    private lateinit var trackTimeRunnable: Runnable

    private val handler = Handler(Looper.getMainLooper())
    private var _playerState = EPlayerState.DEFAULT
    private val _screenState = MutableLiveData<PlayerScreenState>()
    private var _currentTrackTime = "00:00"
    private var _playButtonState = R.drawable.play_icon

    private val dataFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }
    val screenState: LiveData<PlayerScreenState>
        get() = _screenState

    init {
        setupPlayer()
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

    private fun updateUI() {
        handler.post {
            when (_playerState) {
                EPlayerState.DEFAULT -> {}
                EPlayerState.ERROR -> {
                    _screenState.value = PlayerScreenState.Error(track, _currentTrackTime, _playButtonState)
                }
                EPlayerState.PREPARED, EPlayerState.PAUSED -> {
                    _playButtonState = R.drawable.play_icon
                    if (_playerState == EPlayerState.PREPARED) {
                        _currentTrackTime = "00:00"
                        stopTrackTimeRunnable()
                    }
                    _screenState.value =
                        PlayerScreenState.Content(track, _currentTrackTime, _playButtonState)
                }
                EPlayerState.PLAYING -> {
                    _playButtonState = R.drawable.pause_icon
                    _screenState.value =
                        PlayerScreenState.Content(track, _currentTrackTime, _playButtonState)
                }
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
        startTrackTimeRunnable()
    }

    fun pause() {
        playerInteractor.pause()
        stopTrackTimeRunnable()
    }

    private fun startTrackTimeRunnable() {
        trackTimeRunnable = createTrackTimeRunnable()
        handler.post(trackTimeRunnable)
    }

    private fun stopTrackTimeRunnable() {
        if (!this::trackTimeRunnable.isInitialized) return

        handler.removeCallbacks(trackTimeRunnable)
    }

    private fun createTrackTimeRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                handler.post {
                    _currentTrackTime = dataFormat.format(playerInteractor.getCurrentPosition())
                    _screenState.value = PlayerScreenState.Content(track, _currentTrackTime, _playButtonState)
                }

                if (_playerState == EPlayerState.PLAYING)
                    handler.postDelayed(this, TRACK_TIME_UPDATE_DELAY)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTrackTimeRunnable()
        playerInteractor.release()
    }

    companion object {
        private const val TRACK_TIME_UPDATE_DELAY = 500L
    }
}