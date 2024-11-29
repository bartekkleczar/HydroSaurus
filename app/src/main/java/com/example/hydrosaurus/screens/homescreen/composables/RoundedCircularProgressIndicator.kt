package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.checkIfAbleToFloat

@Composable
fun RoundedCircularProgressIndicator(
    progress: MutableState<Int>,
    goal: MutableState<String>,
    modifier: Modifier = Modifier.size(280.dp),
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    strokeWidth: Float = 100f
) {
    val afterMaximumColor = MaterialTheme.colorScheme.tertiary
    Box(modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape
        )
        .size(320.dp),
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = modifier.size(100.dp)
        ) {
            val progressState = progress.value.toFloat()/goal.value.checkIfAbleToFloat()
            drawArc(
                color = if (progressState > 1f) afterMaximumColor else color,
                startAngle = -225f,
                sweepAngle = 270 * progressState,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                )
            )
        }
    }

}