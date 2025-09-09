package com.alijafari.brik.block.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alijafari.brik.ui.theme.BRIKTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun BlockScreen(
    sessionScreenState: BlockScreenState?,
    modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        sessionScreenState?.apply {

            val total by totalSeconds.collectAsState()
            val remaining by remainingSeconds.collectAsState()

            Column (modifier = modifier.fillMaxSize().background(Color.Black),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Text("$remaining\nof\n$total", color = Color.White, textAlign = TextAlign.Center, fontSize = 50.sp, fontWeight = FontWeight.Bold, lineHeight = 55.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BRIKTheme() {
        BlockScreen(
            BlockScreenState(
                totalSeconds = MutableStateFlow(50),
                remainingSeconds = MutableStateFlow(50)
            )
        )
    }
}
data class BlockScreenState(
    var totalSeconds: StateFlow<Int>,
    var remainingSeconds: StateFlow<Int>
)