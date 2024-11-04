package com.example.hydrosaurus.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.hydrosaurus.viewModels.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel, navController: NavController) {
    Button(onClick = {
        authViewModel.signOut()
        navController.navigate("auth")
    }) {
        Text("Sign Out")
    }
}