package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrosaurus.getDarkerBlue
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate


@Composable
fun MonthGrid(
    firestoreViewModel: FirestoreViewModel,
    year: Int = LocalDate.now().year,
    month: Int = LocalDate.now().monthValue
) {
    val records = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    LaunchedEffect(Unit) {
        while (true) {
            firestoreViewModel.getFromUserListOfRecordsAccMonths(
                year,
                month
            ) { eachMonth ->
                records.value = eachMonth
            }
            delay(1000)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(
                color = Color(0x2F000000),
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val monthName = LocalDate.of(year, month, 1).month.toString()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = monthName, color = Color.Black, fontSize = 30.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                for (i in listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")) {
                    Row(
                        modifier = Modifier.width(55.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = i, color = Color.Black, fontSize = 20.sp)
                    }
                }

            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 55.dp),
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
        ) {
            items(records.value) { day ->
                VerticalGridItem(day = day)
            }
        }
    }
}

@Composable
fun VerticalGridItem(day: Map<String, Any>) {
    Box(
        modifier = Modifier
            .size(55.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(
                    color = getDarkerBlue(
                        day["amount"]
                            .toString()
                            .toInt(), day["dayOfMonth"].toString()
                    ),
                    shape = CircleShape
                )
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = day["dayOfMonth"].toString(), color = Color.Black)
        }
    }
}

@Preview
@Composable
fun MonthGridPreview() {
    val firestoreViewModel: FirestoreViewModel = viewModel()
    MonthGrid(firestoreViewModel)
}
