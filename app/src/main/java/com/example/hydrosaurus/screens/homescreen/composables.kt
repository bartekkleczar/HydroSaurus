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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddWaterTile(waterAmount: MutableState<Int>) {
    Box(
        Modifier.background(color = MaterialTheme.colorScheme.onSecondary)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var isChanged = remember { mutableStateOf(true) }
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current

            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        isChanged.value = false
                        coroutineScope.launch {
                            delay(400)
                            waterAmount.value += 50
                            delay(100)
                            isChanged.value = true
                        }
                    }
            )

            AnimatedVisibility(
                visible = isChanged.value,
                enter = slideInHorizontally(animationSpec = tween(300)),
                exit = slideOutHorizontally(animationSpec = tween(300))
            ) {
                Text(
                    text = "${waterAmount.value}ml",
                    fontSize = 25.sp,
                )
            }

            Image(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = "Minus",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        if (waterAmount.value <= 50) {
                            Toast
                                .makeText(
                                    context,
                                    "Water amount can't be bellow 50ml",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            isChanged.value = false
                            coroutineScope.launch {
                                delay(400)
                                waterAmount.value -= 50
                                delay(100)
                                isChanged.value = true
                            }
                        }
                    }
            )
        }
    }
}

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
fun WaterElementCard(record: HashMap<String, Any>, modifier: Modifier) {
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
    record: HashMap<String, Any>,
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
                    .clickable { onDelete() }
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