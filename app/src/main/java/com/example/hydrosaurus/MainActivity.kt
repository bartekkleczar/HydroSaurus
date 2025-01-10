package com.example.hydrosaurus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrosaurus.screens.AuthenticationScreen
import com.example.hydrosaurus.screens.CreatingAccountScreen
import com.example.hydrosaurus.screens.homescreen.HomeScreen
import com.example.hydrosaurus.screens.IntroductionScreen
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.CreatingAccountViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import com.example.hydrosaurus.viewModels.IntroductionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val notificationChannel = NotificationChannel(
            "notification_channel_id",
            "Notification name",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        setContent {
            HydroSaurusTheme {
                val postNotificationPermission = rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }

                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val creatingAccountViewModel: CreatingAccountViewModel = viewModel()
                val firestoreViewModel: FirestoreViewModel = viewModel()
                val introductionViewModel: IntroductionViewModel = viewModel()

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
                            IntroductionScreen(navController = navController, introductionViewModel = introductionViewModel)
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