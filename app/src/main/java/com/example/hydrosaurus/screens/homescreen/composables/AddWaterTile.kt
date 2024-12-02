package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrosaurus.R

@Composable
fun AddWaterTile(waterAmount: MutableState<Int>) {
    Box{
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            AmountGlass(waterAmount, 50)
            AmountGlass(waterAmount, 100)
            AmountGlass(waterAmount, 200)
            AmountGlass(waterAmount, 500)
        }
    }
}

@Composable
fun AmountGlass(waterAmount: MutableState<Int>, addAmount: Int){
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.wateraddicon),
            contentDescription = "Add $addAmount",
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    waterAmount.value += addAmount
                }
        )
        Text(
            text = "$addAmount",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp),
            color = Color.Black
        )
    }
}