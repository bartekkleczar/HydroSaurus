package com.example.hydrosaurus.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hydrosaurus.R
import com.example.hydrosaurus.containsChar
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
            isError = email.isNotEmpty() && !(email.containsChar(x = '@')),
            supportingText = {
                if (email.isNotEmpty() && !(email.containsChar(x = '@'))) Text(text = "Must contain \"@\" sign")
            },
            modifier = Modifier.padding(top = 25.dp).fillMaxWidth(0.9f),
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

            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(65.dp))
        Row{
            val context = LocalContext.current
            Button(onClick = {
                creatingAccountViewModel.goToSignInUserWithEmailAndPassword()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back", Modifier.size(35.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                if (!creatingAccountViewModel.createUserWithEmailAndPassword(email, password)) {
                    Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                } else{
                    navController.navigate("introduction")
                }
            }) {
                Text(text = "Create Account", fontSize = 30.sp)
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    CreatingAccountScreen(CreatingAccountViewModel(), rememberNavController())
}