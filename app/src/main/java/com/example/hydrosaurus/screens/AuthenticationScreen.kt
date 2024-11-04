package com.example.hydrosaurus.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import com.example.hydrosaurus.viewModels.AuthState
import com.example.hydrosaurus.viewModels.AuthViewModel

@Composable
fun AuthenticationScreen(authViewModel: AuthViewModel, navController: NavHostController) {
    val authState = authViewModel.authState.collectAsState()

    LaunchedEffect(key1 = authState.value) {
        if (authState.value == AuthState.AUTHENTICATED) {
            navController.navigate("home")
        }
    }

    when (authState.value) {
        AuthState.UNAUTHENTICATED -> {
            SignInScreen(authViewModel)
        }
        AuthState.AUTHENTICATED -> {
            HomeScreen(authViewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { authViewModel.signInWithEmailAndPassword(email, password) }) {
            Text("Sign In")
        }
        Button(onClick = { authViewModel.createUserWithEmailAndPassword(email, password) }) {
            Text("Sign Up")
        }
    }
}


