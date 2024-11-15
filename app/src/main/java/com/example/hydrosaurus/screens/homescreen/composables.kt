package com.example.hydrosaurus.screens.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun RoundedCircularProgressIndicator(
    progress: MutableState<Float>,
    modifier: Modifier = Modifier.size(250.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 100f
) {
    Canvas(
        modifier = modifier.size(100.dp)
    ) {
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360 * progress.value,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
            )
        )
    }
}

@Composable
fun WaterElementCard(modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.glassofwater),
                contentDescription = "Glass of water",
                Modifier.size(30.dp)
            )
            Row {
                Text(text = "Time")
            }
        }
    }
}

@Composable
fun SwipeableWaterElementCard(
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxDragOffset = 200f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Image(
                painter = painterResource(id = R.drawable.rubbish),
                contentDescription = "delete",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onDelete() }
            )
        }
        val coroutineScope = rememberCoroutineScope()
        WaterElementCard(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-maxDragOffset, 0f)
                        },
                        onDragEnd = {
                            if (offsetX < -maxDragOffset / 1.3) {
                                offsetX = -maxDragOffset
                                coroutineScope.launch {
                                    delay(5000)
                                    offsetX = 0f
                                }
                            } else {
                                offsetX = 0f
                            }
                        }
                    )
                }

        )
    }
}