package com.example.hydrosaurus.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreViewModel() : ViewModel() {

    fun setup(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun createUserDocument(name: String, email: String) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (uid != null) {
            val user = hashMapOf(
                "name" to name,
                "email" to email,
                "totalWater" to 0
            )
            db.collection("users").document(uid).set(user)
                .addOnSuccessListener { Log.d("Firestore", "User data saved successfully") }
                .addOnFailureListener { e -> Log.w("Firestore", "Error saving user data", e) }

        }
    }

    fun readUserDocument(){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Firestore", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
                .addOnFailureListener { exception ->
                    Log.d("Firestore", "get failed with ", exception)
                }
        }
    }
}