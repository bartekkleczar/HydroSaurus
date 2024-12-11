package com.example.hydrosaurus.screens.homescreen.composables

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun BarChart(firestoreViewModel: FirestoreViewModel, modifier: Modifier = Modifier) {
    val weeklySums = remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }

    LaunchedEffect(Unit) {
        firestoreViewModel.getSumOfAmountsForLastWeek { sums ->
            weeklySums.value = sums
        }
    }
    val week = remember { mutableStateOf<MutableMap<String, Int>>(mutableMapOf()) }
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .background(
                color = Color(0x2F000000),
                shape = RoundedCornerShape(20.dp)
            ),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceAround,

    ) {
        weeklySums.value.forEach { pair ->
            week.value[pair.first] = pair.second
            val day = LocalDateTime.now()
            val dayShortcut = "${day.dayOfWeek.toString()[0]}${day.dayOfWeek.toString()[1]}${day.dayOfWeek.toString()[2]}"
            Bar(
                value = pair.second.toFloat(),
                label = if(pair.first == dayShortcut) "TODAY" else pair.first,
                color = if(pair.first == dayShortcut) Color(0xFF0072FF) else Color(0xFF00C6FF)
            )
        }
    }
}

@Composable
fun Bar(
    value: Float,
    label: String,
    color: Color
) {
    val barHeight = remember { mutableStateOf(value/10) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(text = value.toInt().toString(), color = Color.Black)
        Spacer(
            modifier = Modifier
                .height(barHeight.value.dp)
                .width(20.dp)
                .background(color, RoundedCornerShape(6.dp))
        )
        Text(text = label, color = Color.Black)
    }
}
