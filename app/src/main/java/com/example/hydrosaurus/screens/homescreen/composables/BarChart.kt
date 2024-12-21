package com.example.hydrosaurus.screens.homescreen.composables

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.checkIfAbleToFloat
import com.example.hydrosaurus.safeToFloat
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun BarChart(firestoreViewModel: FirestoreViewModel, modifier: Modifier = Modifier) {
    val weeklySums = remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    val maxHeight = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            firestoreViewModel.getSumOfAmountsForLastWeek { sums ->
                weeklySums.value = sums
            }
            firestoreViewModel.getFromUserDocumentProperty("goal", maxHeight)
            delay(100)
        }
    }
    val week = remember { mutableStateOf<MutableMap<String, Int>>(mutableMapOf()) }



    Box(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = Color(0x2F000000),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround,

            ) {
            weeklySums.value.forEach { pair ->
                week.value[pair.first] = pair.second
                val day = LocalDateTime.now()
                val dayShortcut =
                    "${day.dayOfWeek.toString()[0]}${day.dayOfWeek.toString()[1]}${day.dayOfWeek.toString()[2]}"
                Bar(
                    value = pair.second.toFloat(),
                    label = if (pair.first == dayShortcut) "TODAY" else pair.first,
                    color = if (pair.first == dayShortcut) Color(0xFF0072FF) else Color(0xFF00C6FF),
                    maxHeight = maxHeight
                )
            }
        }
    }
}

@Composable
fun Bar(
    value: Float,
    label: String,
    color: Color,
    maxHeight: MutableState<String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Text(text = value.toInt().toString(), color = Color.Black)
        Spacer(
            modifier = Modifier
                .height(
                    if(((value / maxHeight.value.safeToFloat())/10) > 100) {
                    110.dp
                }else ((value / maxHeight.value.safeToFloat())/10).dp)
                .width(20.dp)
                .background(color, RoundedCornerShape(6.dp))
        )
        Text(text = label, color = Color.Black)
    }
}
