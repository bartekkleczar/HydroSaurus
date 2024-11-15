package com.example.hydrosaurus.screens.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.R


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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WaterElementCard(){
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .height(60.dp),
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
            Row{
                Text(text = "Time")
                Spacer(modifier = Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.rubbish),
                    contentDescription = "Rubbish",
                    Modifier.size(20.dp)
                )
            }
        }
    }
}