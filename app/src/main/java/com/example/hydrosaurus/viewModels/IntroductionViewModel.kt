package com.example.hydrosaurus.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class IntroductionViewModel: ViewModel() {
    fun finishIntroduction(navController: NavController){
        navController.navigate("auth")
    }

    fun createUserDocument(name: String, goal: Float) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        Log.d("Firebase", "uid: $uid, user: ${listOf(name, goal)}, ")

        if (uid != null) {
            val user = hashMapOf(
                "name" to name,
                "goal" to goal,
                "totalWater" to 0,
            )
            db.collection("users").document(uid).set(user)
                .addOnSuccessListener { Log.d("Firestore", "User data saved successfully") }
                .addOnFailureListener { e -> Log.e("Firestore", "Error saving user data", e) }

        }
    }
}