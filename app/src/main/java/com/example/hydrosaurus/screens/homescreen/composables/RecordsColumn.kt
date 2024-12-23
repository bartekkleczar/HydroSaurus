package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun RecordsColumn(
    waterAmount: Int,
    year: Int = LocalDateTime.now().year,
    month: Int = LocalDateTime.now().monthValue,
    day: Int = LocalDateTime.now().dayOfMonth,
    firestoreViewModel: FirestoreViewModel
) {
    val recordsList by firestoreViewModel.recordsList.collectAsState()
    LaunchedEffect(Unit) {
        firestoreViewModel.listenForUserListOfRecordsAccDays(
            year, month, day
        )
    }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(430.dp)
            .padding(vertical = 10.dp)
            .background(color = Color(0x2F000000), shape = RoundedCornerShape(20.dp)),
        verticalArrangement = Arrangement.Top
    ) {
        items(recordsList) { record ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(
                        bottom = if (recordsList.indexOf(record) == recordsList.size - 1) 0.dp else 20.dp,
                        top = if (recordsList.indexOf(record) == 0) 20.dp else 0.dp
                    )
                    .height(60.dp),
            ) {
                SwipeableWaterElementCard(record) {
                    firestoreViewModel.deleteFromUserCertainRecord(
                        year = record["year"].toString().toInt(),
                        month = record["monthValue"].toString().toInt(),
                        day = record["dayOfMonth"].toString().toInt(),
                        hour = record["hour"].toString().toInt(),
                        minute = record["minute"].toString().toInt(),
                        sec = record["second"].toString().toInt(),
                        context = context
                    )
                }
            }
        }
    }
}
