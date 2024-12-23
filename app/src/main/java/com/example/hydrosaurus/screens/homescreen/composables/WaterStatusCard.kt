package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WaterStatusCard(
    waterAmount: Int,
    addWaterAmount: MutableState<Int>,
    goal: MutableState<String>,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .background(color = Color(0x2F000000), shape = CircleShape)
        ) {
            RoundedCircularProgressIndicator(
                progress = waterAmount,
                goal = goal,
                waterAmount = waterAmount,
                size = 250
            )
        }
        AddWaterIcon(onClick, addWaterAmount)
    }
}