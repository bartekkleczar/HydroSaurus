package com.example.hydrosaurus.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

open class FirestoreViewModel() : ViewModel() {

    fun setup(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun getFromUserDocumentProperty(property: String, state: MutableState<String>) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("Firestore", "uid: $uid | email: ${auth.currentUser!!.email}")
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

    fun putUserRecord(amount: Int) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val time = LocalDateTime.now()
        if (uid != null) {
            val record = hashMapOf(
                "amount" to amount,
                "dayOfWeek" to time.dayOfWeek,
                "month" to time.month,
                "hour" to time.hour,
                "year" to time.year,
                "dayOfMonth" to time.dayOfMonth,
                "dayOfYear" to time.dayOfYear,
                "monthValue" to time.monthValue,
                "minute" to time.minute,
                "second" to time.second,
            )

            db.collection(uid).document(time.toString()).set(record)
                .addOnSuccessListener { Log.d("Firestore", "$record") }
                .addOnFailureListener { e -> Log.e("Firestore", "Error saving record data", e) }
        }
    }

    /*val input = "23/11/2024 20:33"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val localDateTime = LocalDateTime.parse(input, formatter)
    Log.e("Parsed Time", localDateTime.dayOfMonth.toString())*/

    fun getFromUserCertainRecord(
        stateAmount: MutableState<Int>,
        year: Int = 0,
        month: Int = 0,
        day: Int = 0
    ) {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (uid != null) {
            stateAmount.value = 0
            db.collection(uid).whereEqualTo("year", year).whereEqualTo("monthValue", month).whereEqualTo("dayOfMonth", day).get().addOnSuccessListener {
                docs ->
                for(doc in docs){
                    stateAmount.value += doc.data["amount"].toString().toInt()
                    Log.d("Firestore", "${stateAmount.value} += ${doc.data["amount"].toString().toInt()}")
                }
            }
        }
    }
}