package com.alijafari.brik.block.domain

import kotlinx.coroutines.flow.MutableStateFlow

interface SessionTimer {

    var remainingMillis: MutableStateFlow<Long>
    var totalMillis : MutableStateFlow<Long>
    fun start(durationMillis: Long)
    fun extend(extraMillis: Long)
    fun stop()

    fun addOnTickListener(listener: (millisUntilFinished: Long) -> Unit)
    fun addOnFinishedListener(listener: () -> Unit)
}
