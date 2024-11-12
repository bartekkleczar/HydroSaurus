package com.example.hydrosaurus.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.hydrosaurus.ui.theme.displayFontFamily
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    firestoreViewModel: FirestoreViewModel
) {
    Scaffold(
        topBar = {
                 TopAppBar(title = {
                     Text(
                         text = "WELCOME NAME",
                         fontSize = 25.sp,
                         fontWeight = FontWeight.Bold ,
                         textAlign = TextAlign.Center,
                         color = MaterialTheme.colorScheme.primary,
                         modifier = Modifier.fillMaxWidth()
                     )
                 })
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val sizeOfIcons = 45
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
            var progress by remember { mutableStateOf(0.1f) }
            Box {
                RoundedCircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(250.dp)
                )
                Column(
                    modifier = Modifier.align(Alignment.Center),
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

                        Image(
                            painter = painterResource(id = R.drawable.waterdrop),
                            contentDescription = "Glass of water",
                            Modifier
                                .size(100.dp)
                                .padding(top = 30.dp)
                                .clickable { progress += 0.1f }
                        )
                }
            }
            Button(onClick = {
                authViewModel.signOut()
                navController.navigate("auth")
            }) {
                Text("Sign Out")
            }
            Spacer(modifier = Modifier.height(50.dp))
            LazyColumn {
                items(5) { item ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                            .height(60.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.glassofwater),
                                contentDescription = "Glass of water",
                                Modifier.size(30.dp)
                            )
                            Row{
                                Text(text = "Time")
                                Spacer(modifier = Modifier.width(20.dp))
                                Image(
                                    painter = painterResource(id = R.drawabl.rubbish),
                                    contentDescription = "Rubbish",
                                    Modifier.size(20.dp)
                                )
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

@Composable
fun RoundedCircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 100f
) {
    Canvas(
        modifier = modifier.size(100.dp)
    ) {
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360 * progress,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
            )
        )
    }

}