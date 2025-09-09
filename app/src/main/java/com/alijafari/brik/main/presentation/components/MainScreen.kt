package com.alijafari.brik.main.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alijafari.brik.block.presentation.components.wheel_picker_compose.WheelTimePicker
import com.alijafari.brik.ui.theme.BRIKTheme
import com.alijafari.brik.block.presentation.components.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalTime

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onDurationChanged: (Int) -> Unit,
    onStartClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "BLOCK YOUR PHONE FOR" , color = Color.White, fontWeight = FontWeight.Medium, fontSize = 20.sp)
        Spacer(Modifier.height(10.dp))
        val selectorProperties = WheelPickerDefaults.selectorProperties(
            enabled = true,
            shape = RoundedCornerShape(10.dp),
            color = Color.White.copy(alpha = .1f),
            border = BorderStroke(2.dp, Color.White)
        )
        WheelTimePicker(onSnappedTime = {onDurationChanged((it.hour*60+it.minute)*60)}, startTime = LocalTime.of(0,30),rowCount = 3,textColor = Color.White, textStyle = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.W800), size = DpSize(258.dp, 200.dp), selectorProperties = selectorProperties)
        TextButton(
            onClick = {
                onStartClicked()
            }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .clickable(
                        enabled = true, onClickLabel = "start", onClick = { onStartClicked() }
                    )
                    .fillMaxWidth(.7f)
            ) {
                Text("START", color = Color.Black, fontSize = 23.sp, textAlign = TextAlign.Center, modifier = Modifier
                    .padding(PaddingValues(30.dp, 15.dp))
                    .fillMaxWidth())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BRIKTheme {

        MainScreen(
            onDurationChanged = {},
            onStartClicked = {}
        )
    }
}