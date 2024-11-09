package com.example.hydrosaurus.viewModels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class IntroductionViewModel: ViewModel() {
    fun finishIntroduction(navController: NavController){
        navController.navigate("auth")
    }
}