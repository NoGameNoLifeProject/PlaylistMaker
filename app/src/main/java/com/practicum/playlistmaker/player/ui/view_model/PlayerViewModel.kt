package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    private val _currentTrackTime = MutableLiveData<String>()
    private val _playButtonState = MutableLiveData<Int>()

    private val dataFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }
    val screenState: LiveData<PlayerScreenState>
        get() = _screenState

    val currentTrackTime: LiveData<String>
        get() = _currentTrackTime

    val playButtonState: LiveData<Int>
        get() = _playButtonState


    init {
        setupPlayer()
    }

    private fun setupPlayer() {
        if (track.previewUrl.isNullOrEmpty()) {
            _screenState.value = PlayerScreenState.Error(track)
            return
        }
        _screenState.value = PlayerScreenState.Content(track)

        playerInteractor.setOnStateChangeListener {
            _playerState = it
            updateUI()
        }
        playerInteractor.prepare(track.previewUrl)
    }

    private fun updateUI() {
        val resId = when (_playerState) {
            EPlayerState.DEFAULT, EPlayerState.ERROR -> return
            EPlayerState.PREPARED, EPlayerState.PAUSED -> R.drawable.play_icon
            EPlayerState.PLAYING -> R.drawable.pause_icon
        }
        _playButtonState.postValue(resId)
    }

    fun handlePlayback() {
        when (_playerState) {
            EPlayerState.DEFAULT, EPlayerState.ERROR -> {}
            EPlayerState.PREPARED, EPlayerState.PAUSED -> {
                playerInteractor.play()
                startTrackTimeRunnable()
            }

            EPlayerState.PLAYING -> {
                playerInteractor.pause()
                stopTrackTimeRunnable()
            }
        }
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
                _currentTrackTime.postValue(dataFormat.format(playerInteractor.getCurrentPosition()))

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
        fun getViewModelFactory(
            track: Track,
            playerInteractor: IPlayerInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track, playerInteractor)
            }
        }
    }
}