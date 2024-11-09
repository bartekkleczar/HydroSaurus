package com.example.hydrosaurus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrosaurus.screens.AuthenticationScreen
import com.example.hydrosaurus.screens.CreatingAccountScreen
import com.example.hydrosaurus.screens.HomeScreen
import com.example.hydrosaurus.screens.IntroductionScreen
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.CreatingAccountViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            HydroSaurusTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val creatingAccountViewModel: CreatingAccountViewModel = viewModel()
                val firestoreViewModel: FirestoreViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("auth") {
                            AuthenticationScreen(
                                authViewModel = authViewModel,
                                creatingAccountViewModel,
                                firestoreViewModel,
                                navController
                            )
                        }
                        composable("creating") {
                            CreatingAccountScreen(creatingAccountViewModel, navController)
                        }
                        composable("home") {
                            HomeScreen(authViewModel = authViewModel, navController = navController, firestoreViewModel = firestoreViewModel)
                        }
                        composable("introduction") {
                            IntroductionScreen(navController = navController)
                        }
                    }

                    val auth = FirebaseAuth.getInstance()

                    val currentUser = auth.currentUser
                    if (currentUser == null && navController.currentDestination != navController.findDestination("auth")) {
                        authViewModel.authenticate()
                        navController.navigate("auth")
                    }
                    firestoreViewModel.setup(this)
                }
            }
        }
    }
}

@Preview
@Composable
fun Prewiev() {
    HydroSaurusTheme {
        val navController = rememberNavController()
        val authViewModel: AuthViewModel = viewModel()
        val creatingAccountViewModel: CreatingAccountViewModel = viewModel()
        val firestoreViewModel: FirestoreViewModel = viewModel()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "auth") {
                composable("auth") {
                    AuthenticationScreen(
                        authViewModel = authViewModel,
                        creatingAccountViewModel,
                        firestoreViewModel,
                        navController
                    )
                }
                composable("creating") {
                    CreatingAccountScreen(creatingAccountViewModel, navController)
                }
                composable("home") {
                    HomeScreen(
                        authViewModel = authViewModel,
                        navController = navController,
                        firestoreViewModel = firestoreViewModel
                    )
                }
            }
        }
    }
}