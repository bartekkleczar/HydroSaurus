package com.example.hydrosaurus.screens.homescreen.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate


@Composable
fun MonthGrid(firestoreViewModel: FirestoreViewModel){
    val month = remember{mutableStateOf<List<Map<String, Any>>>(emptyList())}
    LaunchedEffect(Unit){
        while(true){
            firestoreViewModel.getFromUserListOfRecordsAccMonths(
                LocalDate.now().year,
                LocalDate.now().monthValue
            ) { mon ->
                month.value = mon
                //Log.d("MonthGrid", "${month.value}")
            }
            delay(100)
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 60.dp),
        modifier = Modifier.height(400.dp)
    ){
        var i = 0
        items(month.value){ day->
            i += 1
            //Log.d("MonthGrid", "${day["dayOfMonth"]} -- $i")
            VerticalGridItem(day = day)
        }
    }
}

@Composable
fun VerticalGridItem(day: Map<String, Any>){
    Box(
        modifier = Modifier
            .size(60.dp)
            .padding(horizontal = 10.dp)
    ){
        Column{
            Text(text = day["dayOfMonth"].toString())
            Text(text = day["amount"].toString())
        }
    }
}

@Preview
@Composable
fun MonthGridPreview(){
    val firestoreViewModel: FirestoreViewModel = viewModel()
    MonthGrid(firestoreViewModel)
}
