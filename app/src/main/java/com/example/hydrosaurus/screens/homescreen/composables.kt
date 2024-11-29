package com.example.hydrosaurus.screens.homescreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hydrosaurus.R
import com.example.hydrosaurus.checkIfAbleToFloat
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime



@Composable
fun WaterBottomAppBar(navController: NavController) {
    Row(
        Modifier
            .fillMaxSize()
            .padding(bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        val sizeOfIcons = 35
        Image(
            painter = painterResource(id = R.drawable.notificationbell),
            contentDescription = "Notifications",
            Modifier
                .size(sizeOfIcons.dp)
                .clickable { /*TODO*/ }
        )

        Image(
            painter = painterResource(id = R.drawable.stats),
            contentDescription = "Stats",
            Modifier
                .size(sizeOfIcons.dp)
                .clickable { /*TODO*/ }
        )

        Image(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home",
            Modifier
                .size((sizeOfIcons + 15).dp)
                .clickable { /*TODO*/ }
        )

        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "User",
            Modifier
                .size(sizeOfIcons.dp)
                .clickable { /*TODO*/ }
        )

        Image(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            Modifier
                .size(sizeOfIcons.dp)
                .clickable { /*TODO*/ }
        )
    }
}

@Composable
fun RoundedCircularProgressIndicator(
    progress: MutableState<Int>,
    goal: MutableState<String>,
    modifier: Modifier = Modifier.size(280.dp),
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    strokeWidth: Float = 100f
) {
    val afterMaximumColor = MaterialTheme.colorScheme.tertiary
    Box(modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape
        )
        .size(320.dp),
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = modifier.size(100.dp)
        ) {
            val progressState = progress.value.toFloat()/goal.value.checkIfAbleToFloat()
            drawArc(
                color = if (progressState > 1f) afterMaximumColor else color,
                startAngle = -225f,
                sweepAngle = 270 * progressState,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                )
            )
        }
    }

}

@Composable
fun WaterElementCard(record: Map<String, Any>, modifier: Modifier) {
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
            Row{
                Image(
                    painter = painterResource(id = R.drawable.glassofwater),
                    contentDescription = "Glass of water",
                    Modifier.size(30.dp)
                )
                Text(text = "${record["amount"]}")
            }
            Row {
                Text(text = "Today, ${record["hour"]}:${record["minute"]}")
            }
        }
    }
}

@Composable
fun SwipeableWaterElementCard(
    record: Map<String, Any>,
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
                    .clickable {
                        onDelete()
                        offsetX = 0f
                    }
            )
        }
        val coroutineScope = rememberCoroutineScope()
        WaterElementCard(
            record,
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