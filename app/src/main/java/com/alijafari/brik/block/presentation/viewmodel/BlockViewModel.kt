package com.alijafari.brik.block.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alijafari.brik.block.framework.BlockService
import com.alijafari.brik.block.presentation.components.BlockScreenState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class BlockViewModel(app: Application) : AndroidViewModel(app) {


    var blockScreenState by mutableStateOf<BlockScreenState?>(null)
        private set

    fun bindService(service: BlockService) {
        blockScreenState = BlockScreenState(
            totalSeconds = service.sessionTimer.totalMillis
                .map { (it / 1000).toInt() }
                .stateIn(viewModelScope, SharingStarted.Eagerly, 0),
            remainingSeconds = service.sessionTimer.remainingMillis
                .map { (it / 1000).toInt() }
                .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
        )
    }
}
