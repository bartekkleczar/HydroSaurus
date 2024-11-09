package com.example.hydrosaurus.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hydrosaurus.R
import com.example.hydrosaurus.contains
import com.example.hydrosaurus.viewModels.AuthState
import com.example.hydrosaurus.viewModels.AuthViewModel
import com.example.hydrosaurus.viewModels.CreateState
import com.example.hydrosaurus.viewModels.CreatingAccountViewModel
import com.example.hydrosaurus.viewModels.FirestoreViewModel

@Composable
fun AuthenticationScreen(authViewModel: AuthViewModel, creatingAccountViewModel: CreatingAccountViewModel, firestoreViewModel: FirestoreViewModel, navController: NavHostController) {
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
            if(createState.value == CreateState.NOTCREATING) HomeScreen(
                authViewModel,
                navController,
                firestoreViewModel
            )
            if(createState.value == CreateState.CREATING) HomeScreen(
                authViewModel,
                navController,
                firestoreViewModel
            )
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

        Image(
            painter = painterResource(id = R.drawable.logohydro),
            contentDescription = "Logo Hydrosaurus",
            modifier = Modifier.size(300.dp).padding(bottom =50.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = email.isNotEmpty() && !(email.contains(x = '@')),
            supportingText = {
                if (email.isNotEmpty() && !(email.contains(x = '@'))) Text(text = "Must contain \"@\" sign")
            },
            modifier = Modifier.fillMaxWidth(0.9f).padding(top = 50.dp)

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
            },
            modifier = Modifier.fillMaxWidth(0.9f)

        )
        Spacer(modifier = Modifier.height(50.dp))
        val context = LocalContext.current
        OutlinedButton(onClick = {
            if (!authViewModel.signInWithEmailAndPassword(email, password)) {
                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }) {
            Row{
                Text("Sign In", fontSize = 30.sp)
                Icon(imageVector = Icons.Filled.ArrowUpward, contentDescription = "Arrow leading to signing in", Modifier.size(35.dp))
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            modifier = Modifier.padding(bottom = 60.dp),
            onClick = {
            creatingAccountViewModel.goToCreateUserWithEmailAndPassword()
            Log.d("Authentication", "auth: ${authViewModel.authState.value} create: ${creatingAccountViewModel.createState.value}")
        }) {
            Row{
                Text("Sign Up", fontSize = 30.sp)
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Arrow leading to signing up", Modifier.size(35.dp))
            }
        }
    }
}