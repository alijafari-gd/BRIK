package com.alijafari.brik.main.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.AndroidViewModel
import com.alijafari.brik.block.framework.BlockService
import com.alijafari.brik.block.framework.BlockService.Companion.EXTRA_DURATION_SECONDS
import com.alijafari.brik.block.framework.BlockService.Companion.INTENT_START


class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    companion object{
        const val TAG = "MainViewModel"
    }
    private val _selectedDuration = mutableIntStateOf(0)
    val selectedDuration: State<Int> = _selectedDuration
    fun startMyService() {
        Log.e(TAG, "startMyService", )
        val serviceIntent = Intent(app, BlockService::class.java)
        serviceIntent.action = INTENT_START
        serviceIntent.putExtra(
            EXTRA_DURATION_SECONDS , selectedDuration.value
        )
        app.startService(serviceIntent)
    }

    fun setDuration(duration : Int){
        _selectedDuration.intValue = duration
    }
}