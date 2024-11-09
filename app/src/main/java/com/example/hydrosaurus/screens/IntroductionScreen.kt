package com.example.hydrosaurus.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hydrosaurus.ui.theme.HydroSaurusTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroductionScreen(navController: NavController) {
    HydroSaurusTheme{

        var firstStep by remember { mutableStateOf(true) }

        val colorScheme = MaterialTheme.colorScheme

        val first = colorScheme.tertiary
        val second = colorScheme.secondary
        val third = Color(colorScheme.primary.toArgb()-22)
        val back = colorScheme.background

        var backgroundColor by remember { mutableStateOf(first) }
        AnimatedVisibility(
            visible = firstStep,
            exit = fadeOut(animationSpec = tween(1000))
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {

                var firstMessageVisibility by remember { mutableStateOf(false) }
                var secondMessageVisibility by remember { mutableStateOf(false) }
                var thirdMessageVisibility by remember { mutableStateOf(false) }
                var fourthMessageVisibility by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    secondMessageVisibility = true
                    firstMessageVisibility = false
                    backgroundColor = second
                    delay(2000)
                    thirdMessageVisibility = true
                    firstMessageVisibility = true
                    backgroundColor = third
                    delay(2000)
                    fourthMessageVisibility = true
                    backgroundColor = back
                }


                Text(
                    text = "Nice to see you.",
                    modifier = Modifier.padding(bottom = 10.dp),
                    lineHeight = 40.sp,
                            fontWeight = if (!firstMessageVisibility && !secondMessageVisibility) {
                        FontWeight.Bold
                    } else FontWeight.Normal,
                )
                AnimatedVisibility(visible = secondMessageVisibility, enter = scaleIn(animationSpec = tween(500))) {
                    Text(
                        text = "Let's start taking care about your everyday hydration.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 10.dp),
                        lineHeight = 40.sp,
                        fontWeight = if (!firstMessageVisibility) {
                            FontWeight.Bold
                        } else FontWeight.Normal
                    )
                }
                AnimatedVisibility(visible = thirdMessageVisibility, enter = scaleIn(animationSpec = tween(500))) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Firstly we'll fulfill some information",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 10.dp),
                            lineHeight = 40.sp,
                            fontWeight = if (firstMessageVisibility) {
                                FontWeight.Bold
                            } else FontWeight.Normal
                        )

                    }
                }
                AnimatedVisibility(visible = fourthMessageVisibility, enter = scaleIn(animationSpec = tween(500))) {
                    Button(onClick = {
                        firstStep = false
                    }) {
                        Icon(imageVector = Icons.Filled.Start, contentDescription = "Start", Modifier.size(35.dp))
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = !firstStep,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            var name by remember { mutableStateOf("") }
            var goal by remember { mutableFloatStateOf(0f) }
            var glass by remember { mutableFloatStateOf(0f) }

            var currentStage by remember { mutableStateOf(0) }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Crossfade(targetState = currentStage, label = "DUPADUPA") { stage ->
                    when(stage){
                        0 -> Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "What's your name?",
                                fontWeight = FontWeight.Bold,
                                lineHeight = 40.sp,
                            )
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text(text = "Input your name") },
                                modifier = Modifier.padding(bottom = 15.dp)
                            )
                            Button(onClick = {
                                currentStage += 1
                            }) {
                                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done", Modifier.size(35.dp))
                            }
                        }

                        1 -> Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Hi $name, what's your goal?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp,
                                lineHeight = 50.sp,
                                textAlign = TextAlign.Center
                            )
                            Slider(
                                value = goal,
                                onValueChange = {goal = it},
                                steps = 7,
                                valueRange = 0f..4f
                            )
                            Text(
                                text = "${goal}L",
                                fontWeight = FontWeight.Bold,
                                lineHeight = 40.sp,
                                modifier = Modifier.padding(bottom = 15.dp)
                            )
                            Button(onClick = {
                                currentStage += 1
                            }) {
                                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done", Modifier.size(35.dp))
                            }
                        }

                        2 -> Column(
                            Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                "App counts water in glasses, what is your standard glass size",
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp,
                                lineHeight = 50.sp,
                                textAlign = TextAlign.Center
                            )
                            Slider(
                                value = glass,
                                onValueChange = {glass = it},
                                steps = 19,
                                valueRange = 0f..1000f
                            )
                            Text(
                                text = "${glass}ml",
                                fontWeight = FontWeight.Bold,
                                lineHeight = 40.sp,
                                modifier = Modifier.padding(bottom = 15.dp)
                            )
                            Button(onClick = {
                                navController.navigate("auth")
                            }) {
                                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done", Modifier.size(35.dp))
                            }
                        }
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun IntroductionPreview() {
    //IntroductionScreen(navController = NavController())
}