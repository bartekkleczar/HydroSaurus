package com.example.hydrosaurus.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hydrosaurus.contains
import com.example.hydrosaurus.viewModels.AuthState
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.CreateState
import com.example.hydrosaurus.viewModels.CreatingAccountViewModel

@Composable
fun AuthenticationScreen(authViewModel: AuthViewModel, creatingAccountViewModel: CreatingAccountViewModel, navController: NavHostController) {
    val authState = authViewModel.authState.collectAsState()
    val createState = creatingAccountViewModel.createState.collectAsState()

    LaunchedEffect(key1 = authState.value) {
        if (authState.value == AuthState.AUTHENTICATED && createState.value == CreateState.NOTCREATING) {
            navController.navigate("home")
        }
        if (authState.value == AuthState.UNAUTHENTICATED && createState.value == CreateState.CREATING) {
            navController.navigate("creating")
        }
    }

    when (authState.value) {
        AuthState.UNAUTHENTICATED -> {
            if(createState.value == CreateState.NOTCREATING) SignInScreen(authViewModel, creatingAccountViewModel)
            if(createState.value == CreateState.CREATING) CreatingAccountScreen(creatingAccountViewModel, navController)
        }
        AuthState.AUTHENTICATED -> {
            if(createState.value == CreateState.NOTCREATING) HomeScreen(authViewModel, navController)
            if(createState.value == CreateState.CREATING) HomeScreen(authViewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(authViewModel: AuthViewModel, creatingAccountViewModel: CreatingAccountViewModel) {
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
            label = { Text("Email") },
            isError = email.isNotEmpty() && !(email.contains(x = '@')),
            supportingText = {
                if (email.isNotEmpty() && !(email.contains(x = '@'))) Text(text = "Must contain \"@\" sign")
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isNotEmpty() && (password.length < 6 || password.length > 4096),
            supportingText = {
                if (password.length < 6 && password.isNotEmpty()) Text(text = "Too short")
                else if (password.length > 4096 && password.isNotEmpty()) Text(text = "Too long")
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        OutlinedButton(onClick = { authViewModel.signInWithEmailAndPassword(email, password) }) {
            Row{
                Text("Sign In", fontSize = 20.sp)
                Icon(imageVector = Icons.Filled.ArrowUpward, contentDescription = "Arrow leading to signing in")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            creatingAccountViewModel.goToCreateUserWithEmailAndPassword()
            Log.d("Authentication", "auth: ${authViewModel.authState.value} create: ${creatingAccountViewModel.createState.value}")
        }) {
            Row{
                Text("Sign Up", fontSize = 20.sp)
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Arrow leading to signing up")
            }
        }
    }
}