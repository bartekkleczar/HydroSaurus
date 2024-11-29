package com.example.hydrosaurus.screens.homescreen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hydrosaurus.R

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