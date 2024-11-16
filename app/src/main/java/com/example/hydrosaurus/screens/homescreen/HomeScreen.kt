package com.example.hydrosaurus.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hydrosaurus.R
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    firestoreViewModel: FirestoreViewModel
) {
    var progress = remember { mutableStateOf(0.1f) }
    var t = 0
    var waterAmount = remember { mutableStateOf(50) }
    LaunchedEffect(progress){
        while (true){
            if (t == 0) {
                for (i in 0..18) {
                    progress.value += 0.05f
                    delay(1000)
                }
                t = 1
            }
            if (t == 1) {
                progress.value = 0f
                t = 0
            }
        }
    }
    Scaffold(
        topBar = {
                 TopAppBar(title = {
                     Text(
                         text = "WELCOME NAME",
                         fontSize = 25.sp,
                         fontWeight = FontWeight.Bold ,
                         textAlign = TextAlign.Center,
                         color = MaterialTheme.colorScheme.primary,
                         modifier = Modifier.fillMaxWidth().clickable {
                             authViewModel.signOut()
                             navController.navigate("auth")
                         }
                     )
                 })
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(120.dp),
                actions = {
                    Column{
                        AddWaterTile(waterAmount = waterAmount)
                        Spacer(modifier = Modifier.height(10.dp))
                        WaterBottomAppBar(navController)
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                RoundedCircularProgressIndicator(
                    progress = progress
                )
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(top = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "1000ml",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/2000ml",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(id = R.drawable.waterdrop),
                            contentDescription = "Glass of water",
                            Modifier
                                .size(110.dp)
                                .padding(top = 30.dp)
                                .clickable { progress.value += 0.1f }
                        )
                        Text(
                            text = "${waterAmount.value}ml",
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(10) { item ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                            .height(60.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        SwipeableWaterElementCard(onDelete = {/*TODO*/})
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HydroSaurusTheme {
        HomeScreen(
            authViewModel = AuthViewModel(),
            navController = NavController(LocalContext.current),
            firestoreViewModel = FirestoreViewModel()
        )
    }
}
