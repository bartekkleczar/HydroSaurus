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
fun BarChart(
    modifier: Modifier = Modifier,
    maxHeight: Dp = 200.dp,
    //waterAmount: MutableState<Int>,
    firestoreViewModel: FirestoreViewModel,
    year: Int = LocalDateTime.now().year,
    month: Int = LocalDateTime.now().monthValue,
    day: Int = LocalDateTime.now().dayOfMonth,
){

    val list = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    LaunchedEffect(Unit) {
        while (true) {
            //change function to one with accuracy to weeks
            firestoreViewModel.getFromUserListOfRecordsAccDays(year, month, day) { fetchedList ->
                list.value = fetchedList
            }
            delay(100)
        }
    }

    val borderColor = MaterialTheme.colorScheme.primary
    val density = LocalDensity.current
    val strokeWidth = with(density) { 1.dp.toPx() }

    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                Modifier
                    .fillMaxWidth()
                    .height(maxHeight)
                    .padding(10.dp)
                    .drawBehind {
                        // X-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                        // Y-Axis
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
            ),
        verticalAlignment = Alignment.Bottom
    ) {

        list.value.forEach { record ->
            Bar(
                value = record["amount"].toString().toFloat(),
                color = MaterialTheme.colorScheme.secondary,
                maxHeight = maxHeight
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

@Composable
fun Bar(
    value: Float,
    color: Color,
    maxHeight: Dp
) {

    val itemHeight = remember(value) { value / 10 }
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = value.toString())
        Spacer(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .height(itemHeight.dp)
                .width(10.dp)
                .background(color)
        )
    }

}

