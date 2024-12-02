package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrosaurus.R
import com.example.hydrosaurus.checkIfAbleToFloat

@Composable
fun WaterStatusCard(
    waterAmount: MutableState<Int>,
    addWaterAmount: MutableState<Int>,
    goal: MutableState<String>,
    onClick: () -> Unit
) {
    Row(
    ) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            RoundedCircularProgressIndicator(
                progress = waterAmount,
                goal = goal,
                waterAmount = waterAmount,
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(150.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.waterdrop),
                    contentDescription = "Glass of water",
                    Modifier
                        .size(100.dp)
                        .padding(top = 30.dp)
                        .clickable {
                            onClick()
                        }
                )
                Text(
                    text = "${addWaterAmount.value}ml",
                    fontSize = 20.sp
                )
            }

        }
    }
}