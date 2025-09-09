package com.alijafari.brik.block.presentation

import android.app.KeyguardManager
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.getSystemService
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.alijafari.brik.block.framework.BlockService
import com.alijafari.brik.block.presentation.components.BlockScreen
import com.alijafari.brik.block.presentation.viewmodel.BlockViewModel
import com.alijafari.brik.ui.theme.BRIKTheme

class BlockActivity : ComponentActivity() {
    private lateinit var viewModel: BlockViewModel
    private var blockService: BlockService? = null
    private var bound = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[BlockViewModel::class.java]
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            val keyguardManager = getSystemService(KeyguardManager::class.java)
            keyguardManager.requestDismissKeyguard(this, null)
        }
        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
        setContent {
            BRIKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BlockScreen(
                        modifier = Modifier.fillMaxSize(),
                        sessionScreenState = viewModel.blockScreenState
                    )
                }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BlockService.LocalBinder
            blockService = binder.getService()
            bound = true
            viewModel.bindService(blockService!!)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            blockService = null
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, BlockService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }

    override fun onPause() {
        super.onPause()
        startActivity(Intent(
            applicationContext, BlockActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
    }

    override fun onDestroy() {
        super.onDestroy()
        startActivity(Intent(
            applicationContext, BlockActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}
