package com.debugdesk.bot.presentation.screens.tracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.datamodel.TimerParameter
import com.debugdesk.bot.repo.RoomRepository
import com.debugdesk.bot.utils.timeutil.CountDownTimerImpl
import com.debugdesk.bot.utils.timeutil.Time
import com.debugdesk.bot.utils.timeutil.TimerState
import com.debugdesk.bot.utils.timeutil.getCurrentTime
import com.debugdesk.bot.utils.timeutil.getMilliSeconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackerViewModel(
    private val roomRepo: RoomRepository,
) : ViewModel() {
    var timerParameter: TimerParameter = TimerParameter()
    private var countDownTimer: CountDownTimerImpl = CountDownTimerImpl()

    val notes = roomRepo.allNotes

    private val _time: MutableStateFlow<Time> =
        MutableStateFlow(Time(0, 0, 0, "", true))
    val time: StateFlow<Time> = _time


    private val _isTimerStarted: MutableStateFlow<TimerState> =
        MutableStateFlow(TimerState.NotInitialized)
    val timerState: StateFlow<TimerState> = _isTimerStarted

    init {
        countDownObserver()
        timerStateStateObserver()
    }

    private fun timerStateStateObserver() {
        viewModelScope.launch {
            countDownTimer.isTimerStateStarted.collect {
                Log.e("TAG", "timerStateStateObserver: ${it.name} ")
                _isTimerStarted.tryEmit(it)
            }
        }
    }

    fun getAllNotes() {
        roomRepo.getAllNotes()
    }

    private fun countDownObserver() {
        viewModelScope.launch {
            time.collectLatest {
                while (it.isFinished) {
                    delay(1000L)
                    _time.tryEmit(getCurrentTime())
                }
            }
        }
    }


    fun deleteNote(note: Note) {
        viewModelScope.launch {
            roomRepo.deleteNote(note)
            delay(500)
            roomRepo.getAllNotes()
        }
    }

    fun createNote(note: Note) {
        roomRepo.createNote(note)
    }

    fun updateNote(note: Note) {
        roomRepo.updateNote(note)
    }


    fun handleTimerAndState(timerState: TimerState, timerParameter: TimerParameter) {
        Log.e(TAG, "handleTimerAndState: $timerState,  $timerParameter")
        when (timerState) {
            TimerState.Started, TimerState.Replay -> {
                countDownTimer.cancel()
                countDownTimer = CountDownTimerImpl(
                    getMilliSeconds(
                        timerParameter.timeUnitEnum, timerParameter.time
                    )
                )
                countDownTimer.start()
            }

            TimerState.Paused -> countDownTimer.pause()
            TimerState.Cancel -> countDownTimer.cancel()
            TimerState.Resume -> countDownTimer.resume()
            TimerState.Finished, TimerState.NotInitialized -> {}
        }
        timerStateStateObserver()
        viewModelScope.launch {
            countDownTimer.time.collect { time ->
                Log.e(TAG, "handleTimer: $time ")
                _time.tryEmit(time)
            }
        }

    }

    companion object {
        private const val TAG = "TrackerViewModel"
    }
}