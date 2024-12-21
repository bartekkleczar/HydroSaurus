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
import com.example.hydrosaurus.checkDay
import com.example.hydrosaurus.toRecordMap
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate


@Composable
fun MonthGrid(firestoreViewModel: FirestoreViewModel, year: Int = LocalDate.now().year, month: Int = LocalDate.now().monthValue){
    val records = remember{mutableStateOf<List<Map<String, Any>>>(emptyList())}
    val monthRecords = remember{ mutableStateOf<MutableList<Map<String, Any>>>(mutableListOf()) }
    LaunchedEffect(Unit){
        while(true){
            firestoreViewModel.getFromUserListOfRecordsAccMonths(
                year,
                month
            ) { eachMonth ->
                records.value = eachMonth
                //Log.d("MonthGrid", "${month.value}")
            }
            /*monthRecords.value.clear()
            for(i in 1..31){
                if(records.value.isNotEmpty()){
                    monthRecords.value.add(records.value.checkDay(i){
                        LocalDate.of(year, month, i).toRecordMap()
                    })
                }
            }*/
            delay(1000)
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 60.dp),
        modifier = Modifier.height(400.dp)
    ){
        items(records.value){ day ->

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
