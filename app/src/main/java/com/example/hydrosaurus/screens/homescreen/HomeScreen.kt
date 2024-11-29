package com.example.hydrosaurus.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.hydrosaurus.checkIfAbleToFloat
import com.example.hydrosaurus.screens.homescreen.composables.*
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    firestoreViewModel: FirestoreViewModel
) {

    val addWaterAmount = remember { mutableStateOf(50) }
    val waterAmount = remember { mutableStateOf(0) }
    val name = remember { mutableStateOf("") }
    firestoreViewModel.getFromUserDocumentProperty("name", name)
    val goal = remember { mutableStateOf("") }
    firestoreViewModel.getFromUserDocumentProperty("goal", goal)

    firestoreViewModel.getFromUserCurrentDayAmount(
        waterAmount,
        year = LocalDateTime.now().year,
        month = LocalDateTime.now().monthValue,
        day = LocalDateTime.now().dayOfMonth,
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Welcome ${name.value}!",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            authViewModel.signOut()
                            navController.navigate("auth")
                        }
                )
            })
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(150.dp),
                actions = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        NewAddWaterTile(waterAmount = addWaterAmount)
                        HorizontalDivider(thickness = 2.dp)
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
                .padding(bottom = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                RoundedCircularProgressIndicator(
                    progress = waterAmount,
                    goal = goal
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${waterAmount.value}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/${goal.value.checkIfAbleToFloat()}ml",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.waterdrop),
                            contentDescription = "Glass of water",
                            Modifier
                                .size(110.dp)
                                .padding(top = 30.dp)
                                .clickable {
                                    waterAmount.value += addWaterAmount.value
                                    firestoreViewModel.putUserRecord(addWaterAmount.value)
                                    firestoreViewModel.getFromUserCurrentDayAmount(
                                        waterAmount,
                                        year = LocalDateTime.now().year,
                                        month = LocalDateTime.now().monthValue,
                                        day = LocalDateTime.now().dayOfMonth,
                                    )
                                    addWaterAmount.value = 50
                                }
                        )
                        Text(
                            text = "${addWaterAmount.value}ml",
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(40.dp)
                    )
            ) {
                RecordsLazyColumn(waterAmount, firestoreViewModel = firestoreViewModel)
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
