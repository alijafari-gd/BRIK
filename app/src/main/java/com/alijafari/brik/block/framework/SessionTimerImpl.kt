package com.alijafari.brik.block.framework

import android.os.CountDownTimer
import com.alijafari.brik.block.domain.SessionTimer
import kotlinx.coroutines.flow.MutableStateFlow

class SessionTimerImpl() : SessionTimer {
    private var timer: CountDownTimer? = null
    override var remainingMillis: MutableStateFlow<Long> = MutableStateFlow(0L)
    override var totalMillis: MutableStateFlow<Long> = MutableStateFlow(0L)
    private var onTickListener : (millisUntilFinished : Long)-> Unit = {}
    private var onFinishListener : ()-> Unit = {}

    override fun start(durationMillis: Long) {
        totalMillis.value = durationMillis
        remainingMillis.value = durationMillis
        timer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis.value = millisUntilFinished
                onTickListener(millisUntilFinished)
            }

            override fun onFinish() {
                TODO()
            }
        }.start()
    }

    override fun addOnTickListener(listener : (millisUntilFinished : Long)-> Unit){
        onTickListener = listener
    }

    override fun addOnFinishedListener(listener : () -> Unit){
        onFinishListener = listener
    }

    override fun extend(extraMillis: Long) {
        timer?.cancel()
        totalMillis.value += extraMillis
        remainingMillis.value += extraMillis
        start(remainingMillis.value)
    }

    override fun stop() {
        timer?.cancel()
    }
}