package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 5.dp)
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(20.dp))
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
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