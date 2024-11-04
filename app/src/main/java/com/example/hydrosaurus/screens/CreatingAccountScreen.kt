package com.example.hydrosaurus.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hydrosaurus.contains
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.CreateState
import com.example.hydrosaurus.viewModels.CreatingAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatingAccountScreen(
    creatingAccountViewModel: CreatingAccountViewModel,
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2nd by remember { mutableStateOf("") }

    val createState = creatingAccountViewModel.createState.collectAsState()
    LaunchedEffect(key1 = createState.value) {
        if (createState.value == CreateState.NOTCREATING) {
            navController.navigate("auth")
        }
    }
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
        OutlinedTextField(
            value = password2nd,
            onValueChange = { password2nd = it },
            label = { Text("Password second time") },
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isNotEmpty() && (password != password2nd || password2nd.length < 6 || password2nd.length > 4096),
            supportingText = {
                if (password != password2nd && password.isNotEmpty()) Text(text = "Passwords don\'t match")
                else if (password2nd.length < 6 && password2nd.isNotEmpty()) Text(text = "Too short")
                else if (password2nd.length > 4096 && password2nd.isNotEmpty()) Text(text = "Too long")

            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        Row{
            Button(onClick = {
                creatingAccountViewModel.goToSignInUserWithEmailAndPassword()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                creatingAccountViewModel.createUserWithEmailAndPassword(email, password)
            }) {
                Text(text = "Create Account", fontSize = 20.sp)
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    CreatingAccountScreen(CreatingAccountViewModel(), rememberNavController())
}