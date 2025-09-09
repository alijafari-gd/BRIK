package com.alijafari.brik.main.presentation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.alijafari.brik.main.presentation.components.MainScreen
import com.alijafari.brik.main.viewmodel.MainViewModel
import com.alijafari.brik.ui.theme.BRIKTheme
import com.alijafari.brik.utils.PermissionUtils
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]
        enableEdgeToEdge()

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }

        if (PermissionUtils.needsPermissionScreen(this)){
            startActivity(
                Intent(
                    this, PermissionCheckActivity::class.java
                )
            )
        }
        setContent {
            BRIKTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Color.Black)) { innerPadding ->
                    MainScreen(
                        modifier = Modifier,
                        onDurationChanged = {
                            viewModel.setDuration(it)
                        },
                        onStartClicked = {
                            viewModel.startMyService()
                        })
                }
            }
        }
    }
}