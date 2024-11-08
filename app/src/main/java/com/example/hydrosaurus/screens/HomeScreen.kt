package com.example.hydrosaurus.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    firestoreViewModel: FirestoreViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {
            authViewModel.signOut()
            navController.navigate("auth")
        }) {
            Text("Sign Out")
        }
        Button(onClick = {
            firestoreViewModel.createUserDocument("dupa", "dupa@")
        }) {
            Text("Put data")
        }
        Button(onClick = {
            firestoreViewModel.readUserDocument()
        }) {
            Text("Read data")
        }
    }
}