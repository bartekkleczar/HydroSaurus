package com.example.hydrosaurus.screens.homescreen

import BarChart
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hydrosaurus.R
import com.example.hydrosaurus.screens.homescreen.composables.*
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    firestoreViewModel: FirestoreViewModel
) {

    val addWaterAmount = remember { mutableStateOf(0) }
    val currentWaterAmount by firestoreViewModel.currentWaterAmount.collectAsState()
    val name = remember { mutableStateOf("") }
    firestoreViewModel.getFromUserDocumentProperty("name", name)
    val goal = remember { mutableStateOf("") }
    firestoreViewModel.getFromUserDocumentProperty("goal", goal)

    LaunchedEffect(Unit) {
        val now = LocalDateTime.now()
        firestoreViewModel.listenForUserCurrentDayAmount(
            year = now.year,
            month = now.monthValue,
            day = now.dayOfMonth,
        )
    }

    val items = listOf(
        NavigationItem(
            title = "Home",
            icon = painterResource(id = R.drawable.home)
        ),
        NavigationItem(
            title = "Settings",
            icon = painterResource(id = R.drawable.settings)

        ),
        NavigationItem(
            title = "User",
            icon = painterResource(id = R.drawable.user)
        ),
    )
    val colors =
        if(isSystemInDarkTheme()) {
            listOf(
                Color(0xFF7ABDB2),
                Color(0xFFACB3B3),
            )
        }
        else {
            listOf(
                /*Color(0xFFB0FDF4),
                Color(0xFF05E4D1),
                Color(0xFF0EC4E4)*/
                Color(0xFFD6FFF9),
                Color(0xFF91FFF3),
            )
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = colors
                )
            )
    ) {

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color(0x9F000000)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color.DarkGray
                            ),
                            label = { Text(text = item.title, color = Color.Black) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                //navController.navigate
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Image(
                                    painter = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(35.dp)
                                )
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                        containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Welcome ${name.value}",
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        authViewModel.signOut()
                                        navController.navigate("auth")
                                    }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.Black
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent
                        )
                    )
                },
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WaterStatusCard(
                        waterAmount = currentWaterAmount,
                        addWaterAmount = addWaterAmount,
                        goal = goal
                    ) {
                        firestoreViewModel.putUserRecord(addWaterAmount.value)
                        addWaterAmount.value = 0
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    AddWaterTile(addWaterAmount)
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        RecordsColumn(currentWaterAmount, firestoreViewModel = firestoreViewModel)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    BarChart(firestoreViewModel = firestoreViewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                    MonthGrid(firestoreViewModel = firestoreViewModel)
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
