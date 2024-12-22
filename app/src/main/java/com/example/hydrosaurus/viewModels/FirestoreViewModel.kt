package com.example.hydrosaurus.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.hydrosaurus.checkDay
import com.example.hydrosaurus.minutesCorrection
import com.example.hydrosaurus.toRecordMap
import com.example.hydrosaurus.weekDayToInt
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime

open class FirestoreViewModel() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val uid = auth.currentUser?.uid

    private val _currentWaterAmount = MutableStateFlow(0)
    val currentWaterAmount: StateFlow<Int> = _currentWaterAmount.asStateFlow()

    fun setup(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun getFromUserDocumentProperty(property: String, state: MutableState<String>) {

        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d("Firestore", "uid: $uid | email: ${auth.currentUser!!.email}")
                    //Log.d("Firestore", "DocumentSnapshot data: ${document.data}")
                    state.value = document.data?.get(property).toString()
                } else {
                    Log.e("Firestore", "No such document")
                }
            }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "get failed with ", exception)
                }
        }
    }

    fun putUserRecord(amount: Int) {

        val time = LocalDateTime.now()
        if (uid != null) {
            val record = hashMapOf(
                "amount" to amount,
                "isSum" to false,
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

            db.collection(uid)
                .document("${time.year}-${time.monthValue}-${time.dayOfMonth}T${time.hour}:${time.minute}:${time.second}")
                .set(record)
                .addOnSuccessListener {
                    //Log.d("Firestore", "$record")
                }
                .addOnFailureListener { e -> Log.e("Firestore", "Error saving record data", e) }

            updateUserDayRecord(
                year = time.year,
                monthValue = time.monthValue,
                dayOfMonth = time.dayOfMonth,
                addAmount = amount
            )
        }
    }

    private fun updateUserDayRecord(year: Int, monthValue: Int, dayOfMonth: Int, addAmount: Int) {
        val day = "${year}-${monthValue}-${dayOfMonth}D"

        var amount = 0
        val time = LocalDateTime.now()
        if (uid != null) {
            db.collection(uid).document(day).get().addOnSuccessListener { d ->
                if (d.data?.get("amount") != null) amount = d.data?.get("amount").toString().toInt()
                //Log.d("Firestore", amount.toString())
                setUserDayRecord() {
                    db.collection(uid).document(day).update("amount", amount + addAmount)
                }
            }
        }
    }

    private fun setUserDayRecord(onSuccess: () -> Unit) {
        val time = LocalDateTime.now()
        val day = "${time.year}-${time.monthValue}-${time.dayOfMonth}D"

        var amount = 0
        if (uid != null) {
            val record = hashMapOf(
                "amount" to 0,
                "isSum" to true,
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

            db.collection(uid).document(day).set(record)
                .addOnSuccessListener {
                    //Log.d("Firestore", "$record")
                    onSuccess()
                }
                .addOnFailureListener { e -> Log.e("Firestore", "Error saving record data", e) }
        }
    }

    fun deleteFromUserCertainRecord(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0,
        hour: Int = 0,
        minute: Int = 0,
        sec: Int = 0,
        context: Context
    ) {

        if (uid != null) {
            var amount = 0
            val dayStr = "${year}-${month}-${day}T${hour}:${minute}:${sec}"
            db.collection(uid).document(dayStr).get().addOnSuccessListener { d ->
                amount = d.data?.get("amount").toString().toInt()
                db.collection(uid).document("$year-$month-${day}T$hour:$minute:$sec").delete()
                    .addOnSuccessListener { _ ->
                        updateUserDayRecord(
                            year = year,
                            monthValue = month,
                            dayOfMonth = day,
                            addAmount = -amount
                        )
                        Toast.makeText(
                            context,
                            "Record $hour:${minute.minutesCorrection()}:$sec deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }
    }

    fun listenForUserCurrentDayAmount(
        year: Int,
        month: Int,
        day: Int
    ) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection(uid)
                .whereEqualTo("year", year)
                .whereEqualTo("monthValue", month)
                .whereEqualTo("dayOfMonth", day)
                .whereEqualTo("isSum", false)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("Firestore", "Listen failed.", e)
                        _currentWaterAmount.value = 0
                        return@addSnapshotListener
                    }
                    var newAmount = 0
                    if (snapshot != null) {
                        for (doc in snapshot.documents) {
                            newAmount += doc.data?.get("amount").toString().toInt()
                        }
                        _currentWaterAmount.value = newAmount
                    } else {
                        _currentWaterAmount.value = 0
                    }
                }
        }
    }

    fun getFromUserListOfRecordsAccDays(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0,
        onResult: (List<Map<String, Any>>) -> Unit
    ) {

        val list = mutableListOf<Map<String, Any>>()
        if (uid != null) {
            db.collection(uid)
                .whereEqualTo("year", year)
                .whereEqualTo("monthValue", month)
                .whereEqualTo("dayOfMonth", day)
                .whereEqualTo("isSum", false)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        list.add(
                            mapOf(
                                "amount" to doc.data["amount"].toString().toInt(),
                                "dayOfWeek" to doc.data["dayOfWeek"].toString(),
                                "month" to doc.data["month"].toString(),
                                "hour" to doc.data["hour"].toString().toInt(),
                                "year" to doc.data["year"].toString().toInt(),
                                "dayOfMonth" to doc.data["dayOfMonth"].toString().toInt(),
                                "dayOfYear" to doc.data["dayOfYear"].toString().toInt(),
                                "monthValue" to doc.data["monthValue"].toString().toInt(),
                                "minute" to doc.data["minute"].toString().toInt(),
                                "second" to doc.data["second"].toString().toInt(),
                            )
                        )
                    }
                    onResult(list)
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error fetching data", exception)
                    onResult(emptyList())
                }
        } else {
            onResult(emptyList())
        }
    }

    fun getSumOfAmountsForLastWeek(onResult: (List<Pair<String, Int>>) -> Unit) {
        val today = LocalDate.now()
        val sums = mutableListOf<Pair<String, Int>>()
        val pendingDays = mutableListOf<LocalDate>()

        for (i in 0..6) {
            val day = today.minusDays(i.toLong())
            pendingDays.add(day)
        }

        val resultsCount = arrayOf(0)
        for (day in pendingDays) {
            getFromUserListOfRecordsAccDays(
                year = day.year,
                month = day.monthValue,
                day = day.dayOfMonth
            ) { records ->
                val sumForDay = records.sumOf { it["amount"] as Int }
                sums.add("${day.dayOfWeek.toString()[0]}${day.dayOfWeek.toString()[1]}${day.dayOfWeek.toString()[2]}" to sumForDay)

                resultsCount[0]++
                if (resultsCount[0] == pendingDays.size) {
                    val orderedSums = sums.sortedBy {
                        val order = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
                        order.indexOf(it.first)
                    }
                    onResult(orderedSums)
                }
            }
        }
    }

    fun getFromUserListOfRecordsAccMonths(
        year: Int,
        monthValue: Int,
        onResult: (List<Map<String, Any>>) -> Unit
    ) {

        val list = mutableListOf<Map<String, Any>>()

        if (uid != null) {
            db.collection(uid)
                .whereEqualTo("year", year)
                .whereEqualTo("monthValue", monthValue)
                .whereEqualTo("isSum", true)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        list.add(doc.data)
                    }
                    val sortedList = list.sortedBy {
                        it["dayOfMonth"].toString().toInt()
                    }

                    /*for(el in sortedList){
                        Log.d("Firebase", "${el["dayOfMonth"]} -- ${el["amount"]}")
                    }*/
                    var monthRecords = mutableListOf<Map<String, Any>>()
                    for (i in 1..31) {
                        if (sortedList.isNotEmpty()) {
                            monthRecords.add(sortedList.checkDay(i) {
                                LocalDate.of(year, monthValue, i).toRecordMap()
                            })
                        }
                    }

                    val firstDayOfMonth = LocalDate.of(year, monthValue, 1).toRecordMap()
                    if (firstDayOfMonth["dayOfWeek"] != "MONDAY") {
                        val firstDayNumber = weekDayToInt(firstDayOfMonth["dayOfWeek"].toString())
                        for (i in 1 until firstDayNumber) {
                            monthRecords =
                                (mutableListOf(
                                    mapOf(
                                        "amount" to 0,
                                        "dayOfWeek" to "",
                                        "month" to "",
                                        "hour" to 0,
                                        "year" to "",
                                        "dayOfMonth" to "",
                                        "dayOfYear" to "",
                                        "monthValue" to "",
                                        "minute" to 0,
                                        "second" to 0,
                                    )
                                ) + monthRecords).toMutableList()

                        }
                    }

                    onResult(monthRecords)
                }
        }
    }
}