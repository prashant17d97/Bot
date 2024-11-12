package com.debugdesk.bot.utils.timeutil

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountDownTimerImpl(
    mMillisInFuture: Long = 1000L,
    countDownInterval: Long = TimeUnitEnum.SECOND.millisecondsPerUnit,
) : CountDownTimer(mMillisInFuture, countDownInterval) {

    companion object {
        private const val TAG = "CountDownTimerImpl"
    }


    private val _isTimerStateStarted: MutableStateFlow<TimerState> =
        MutableStateFlow(TimerState.NotInitialized)
    val isTimerStateStarted: StateFlow<TimerState> = _isTimerStateStarted


    private val _time: MutableStateFlow<Time> = MutableStateFlow(
        Time(
            hour = 0,
            minute = 0,
            second = 0,
            format = "",
            isFinished = true
        )
    )

    val time: StateFlow<Time> = _time

    override fun onTick(millisUntilFinished: Long) {
        _time.tryEmit(millisUntilFinished.getCalculatedTime().copy(format = ""))
    }

    fun pause() {
        _isTimerStateStarted.tryEmit(TimerState.Paused)
        onPause()
        _time.tryEmit(onPause().getCalculatedTime().copy(format = ""))

    }

    fun start(): CountDownTimerImpl {
        _isTimerStateStarted.tryEmit(TimerState.Started)
        onStart()
        return this@CountDownTimerImpl

    }

    fun resume() {
        _isTimerStateStarted.tryEmit(TimerState.Resume)
        onResume()
        _time.tryEmit(onResume().getCalculatedTime().copy(format = ""))
    }

    fun cancel() {
        onCancel()
        _isTimerStateStarted.tryEmit(TimerState.NotInitialized)
        _time.tryEmit(getCurrentTime())
    }

    override fun onFinish() {
        _time.tryEmit(getCurrentTime())
        _isTimerStateStarted.tryEmit(TimerState.Finished)
    }

}