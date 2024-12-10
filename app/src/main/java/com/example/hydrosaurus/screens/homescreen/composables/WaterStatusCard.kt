package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrosaurus.R
import com.example.hydrosaurus.checkIfAbleToFloat
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WaterStatusCard(
    waterAmount: MutableState<Int>,
    addWaterAmount: MutableState<Int>,
    goal: MutableState<String>,
    onClick: () -> Unit
) {
    val elementsSize = 190
    Row(Modifier.padding(horizontal = 5.dp)){
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        ) {
            RoundedCircularProgressIndicator(
                progress = waterAmount,
                goal = goal,
                waterAmount = waterAmount,
                size = elementsSize
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .size(elementsSize.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                val scale = remember { mutableStateOf(1f) }
                val scope = rememberCoroutineScope()
                Image(
                    painter = painterResource(id = R.drawable.waterdrop),
                    contentDescription = "Glass of water",
                    Modifier
                        .size(100.dp)
                        .padding(top = 30.dp)
                        .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                        .clickable {
                            onClick()
                            scope.launch {
                                scale.value = 1.2f
                                delay(100)
                                scale.value = 1f
                            }
                        }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${addWaterAmount.value}ml",
                    fontSize = 40.sp,
                    color = Color.Black
                )
            }

        }
    }
}