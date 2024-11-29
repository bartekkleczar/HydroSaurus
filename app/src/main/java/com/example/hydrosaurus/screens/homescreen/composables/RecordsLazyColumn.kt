package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun RecordsLazyColumn(
    waterAmount: MutableState<Int>,
    year: Int = LocalDateTime.now().year,
    month: Int = LocalDateTime.now().monthValue,
    day: Int = LocalDateTime.now().dayOfMonth,
    firestoreViewModel: FirestoreViewModel
) {
    val list = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    LaunchedEffect(Unit) {
        while (true) {
            firestoreViewModel.getFromUserListOfRecordsAccDays(year, month, day) { fetchedList ->
                list.value = fetchedList
            }
            delay(100)
        }
    }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
    ) {
        items(items = list.value) { record ->
            Card(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .height(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
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
                    firestoreViewModel.getFromUserCurrentDayAmount(
                        waterAmount,
                        year = LocalDateTime.now().year,
                        month = LocalDateTime.now().monthValue,
                        day = LocalDateTime.now().dayOfMonth,
                    )
                }
            }
        }
    }
}