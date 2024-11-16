package com.example.hydrosaurus.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class FirestoreViewModel() : ViewModel() {

    fun setup(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    private val _userDocumentContentName = MutableStateFlow("")
    val userDocumentContentName: StateFlow<String> = _userDocumentContentName.asStateFlow()

    fun getFromUserDocumentProperty(property: String){
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Firestore", "DocumentSnapshot data: ${document.data}")

                    _userDocumentContentName.value = document.data?.get(property).toString()

                    Log.d("Firestore", "_userDocumentContent: ${_userDocumentContentName.value}")
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