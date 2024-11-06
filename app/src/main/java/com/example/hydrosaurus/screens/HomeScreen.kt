package com.example.hydrosaurus.screens

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.CreatingAccountViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(authViewModel: AuthViewModel, navController: NavController) {
    Button(onClick = {
        authViewModel.signOut()
        navController.navigate("auth")
    }) {
        Text("Sign Out")
    }
}