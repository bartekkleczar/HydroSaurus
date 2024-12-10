package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrosaurus.R
import com.example.hydrosaurus.checkIfAbleToFloat

@Composable
fun RoundedCircularProgressIndicator(
    progress: MutableState<Int>,
    goal: MutableState<String>,
    strokeWidth: Float = 70f,
    waterAmount: MutableState<Int>,
    size: Int
) {
    Box(modifier = Modifier
        .size(size.dp),
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = Modifier.size(160.dp)
        ) {
            val progressState = progress.value.toFloat()/goal.value.checkIfAbleToFloat()
            val gradientBrush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF00C6FF),
                    Color(0xFF0072FF)
                )
            )

            drawArc(
                brush = gradientBrush,
                startAngle = -225f,
                sweepAngle = 270 * progressState,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${waterAmount.value}ml",
                fontSize = 30.sp,
                color = Color.Black
            )
            Text(
                text = "/${goal.value.checkIfAbleToFloat()}ml",
                fontSize = 30.sp,
                color = Color.Black
            )


        }

    }
}