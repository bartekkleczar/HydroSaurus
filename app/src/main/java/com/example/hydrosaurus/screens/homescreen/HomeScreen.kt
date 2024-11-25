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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    firestoreViewModel: FirestoreViewModel
) {

    var progress = remember { mutableStateOf(0.1f) }
    var t = 0
    var addWaterAmount = remember { mutableStateOf(50) }
    var waterAmount = remember { mutableStateOf(0) }
    var time = remember { mutableStateOf("") }
    //nie ta zmienna lol
    //firestoreViewModel.getFromUserRecord(waterAmount, time)
    var name = remember { mutableStateOf("") }
    firestoreViewModel.getFromUserDocumentProperty("name", name)
    var goal = remember { mutableStateOf("") }
    firestoreViewModel.getFromUserDocumentProperty("goal", goal)

    firestoreViewModel.getFromUserCertainRecord(waterAmount, year = 2024, month = 11, day = 24)

    val recordList = remember { mutableStateOf(mutableListOf<HashMap<String, Any>>()) }
    firestoreViewModel.getFromUserListOfRecordsAccDays(
        recordList,
        year = 2024,
        month = 11,
        day = 24
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "WELCOME ${name.value}!",
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
                modifier = Modifier.height(120.dp),
                actions = {
                    Column {
                        AddWaterTile(waterAmount = addWaterAmount)
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

                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Image(
                            painter = painterResource(id = R.drawable.waterdrop),
                            contentDescription = "Glass of water",
                            Modifier
                                .size(110.dp)
                                .padding(top = 30.dp)
                                .clickable {
                                    waterAmount.value += addWaterAmount.value
                                    firestoreViewModel.putUserRecord(addWaterAmount.value)
                                    firestoreViewModel.getFromUserCertainRecord(
                                        waterAmount,
                                        year = 2024,
                                        month = 11,
                                        day = 24
                                    )
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
                key(recordList) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp)
                    ) {

                        items(items = recordList.value) { record ->
                            Card(
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                                    .height(60.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                SwipeableWaterElementCard(record) {/*TODO*/ }
                            }
                        }
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
