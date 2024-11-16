package com.example.hydrosaurus.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class FirestoreViewModel() : ViewModel() {

    fun setup(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun getFromUserDocumentProperty(property: String, state: MutableState<String>){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Firestore", "DocumentSnapshot data: ${document.data}")
                    state.value = document.data?.get(property).toString()
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