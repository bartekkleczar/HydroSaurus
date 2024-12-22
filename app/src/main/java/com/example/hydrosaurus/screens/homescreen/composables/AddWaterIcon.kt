package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddWaterIcon(
    onClick: () -> Unit,
    addWaterAmount: MutableState<Int>
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 10.dp)
            .size(100.dp)
            .background(color = Color(0x2F000000), shape = RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            val scale = remember { mutableStateOf(1f) }
            val scope = rememberCoroutineScope()
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.waterdrop),
                    contentDescription = "Glass of water",
                    Modifier
                        .size(60.dp)
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
                Text(
                    text = "+",
                    fontSize = 40.sp,
                    color = Color.Black
                )
            }
            Text(
                text = "${addWaterAmount.value}ml",
                fontSize = 30.sp,
                color = Color.Black
            )
        }

    }
}