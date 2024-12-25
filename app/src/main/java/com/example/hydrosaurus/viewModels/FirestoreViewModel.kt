package com.example.hydrosaurus.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydrosaurus.checkDay
import com.example.hydrosaurus.minutesCorrection
import com.example.hydrosaurus.toRecordMap
import com.example.hydrosaurus.weekDayToInt
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import java.time.LocalDateTime

open class FirestoreViewModel() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val uid: String = auth.currentUser!!.uid

    private val _currentWaterAmount = MutableStateFlow(0)
    val currentWaterAmount: StateFlow<Int> = _currentWaterAmount.asStateFlow()

    private val _recordsList = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val recordsList: StateFlow<List<Map<String, Any>>> = _recordsList.asStateFlow()

    private val _lastWeekSums = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val lastWeekSums: StateFlow<List<Pair<String, Int>>> = _lastWeekSums.asStateFlow()

    private val _monthRecords = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val monthRecords: StateFlow<List<Map<String, Any>>> = _monthRecords.asStateFlow()

    fun setup(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun getFromUserDocumentProperty(property: String, state: MutableState<String>) {

        db.collection("users").document(uid).get().addOnSuccessListener { document ->
            if (document != null) {
                state.value = document.data?.get(property).toString()
            } else {
                Log.e("Firestore", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "get failed with ", exception)
            }
    }

    fun putUserRecord(amount: Int) {

        val time = LocalDateTime.now()
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
            }
            .addOnFailureListener { e -> Log.e("Firestore", "Error saving record data", e) }

        updateUserDayRecord(
            year = time.year,
            monthValue = time.monthValue,
            dayOfMonth = time.dayOfMonth,
            addAmount = amount
        )
    }

    private fun updateUserDayRecord(year: Int, monthValue: Int, dayOfMonth: Int, addAmount: Int) {
        val day = "${year}-${monthValue}-${dayOfMonth}D"

        var amount = 0
        db.collection(uid).document(day).get().addOnSuccessListener { d ->
            if (d.data?.get("amount") != null) amount = d.data?.get("amount").toString().toInt()
            setUserDayRecord() {
                db.collection(uid).document(day).update("amount", amount + addAmount)
            }
        }
    }

    private fun setUserDayRecord(onSuccess: () -> Unit) {
        val time = LocalDateTime.now()
        val day = "${time.year}-${time.monthValue}-${time.dayOfMonth}D"

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

    fun deleteFromUserCertainRecord(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0,
        hour: Int = 0,
        minute: Int = 0,
        sec: Int = 0,
        context: Context
    ) {

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

    fun listenForUserCurrentDayAmount(
        year: Int,
        month: Int,
        day: Int
    ) {
        db.collection(uid)
            .whereEqualTo("year", year)
            .whereEqualTo("monthValue", month)
            .whereEqualTo("dayOfMonth", day)
            .whereEqualTo("isSum", true)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Listen failed.", e)
                    _currentWaterAmount.value = 0
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    _currentWaterAmount.value =
                        snapshot.documents[0].data?.get("amount").toString().toInt()
                } else {
                    _currentWaterAmount.value = 0
                }
            }
    }

    fun listenForUserListOfRecordsAccDays(
        year: Int = 0,
        month: Int = 0,
        day: Int = 0,
    ) {
        db.collection(uid)
            .whereEqualTo("year", year)
            .whereEqualTo("monthValue", month)
            .whereEqualTo("dayOfMonth", day)
            .whereEqualTo("isSum", false)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Listen failed.", e)
                    _recordsList.value = emptyList()
                    return@addSnapshotListener
                }
                val list = mutableListOf<Map<String, Any>>()
                if (snapshot != null) {
                    for (doc in snapshot.documents) {
                        list.add(
                            mapOf(
                                "amount" to doc.data?.get("amount").toString().toInt(),
                                "dayOfWeek" to doc.data?.get("dayOfWeek").toString(),
                                "month" to doc.data?.get("month").toString(),
                                "hour" to doc.data?.get("hour").toString().toInt(),
                                "year" to doc.data?.get("year").toString().toInt(),
                                "dayOfMonth" to doc.data?.get("dayOfMonth").toString().toInt(),
                                "dayOfYear" to doc.data?.get("dayOfYear").toString().toInt(),
                                "monthValue" to doc.data?.get("monthValue").toString().toInt(),
                                "minute" to doc.data?.get("minute").toString().toInt(),
                                "second" to doc.data?.get("second").toString().toInt(),
                            )
                        )
                    }
                    _recordsList.value = list
                    getSumOfAmountsForLastWeek()
                } else {
                    _recordsList.value = emptyList()
                }
            }
    }

    private fun getSumOfAmountsForLastWeek() {
        val today = LocalDate.now()
        val currentDayOfWeek = today.dayOfWeek
        val daysUntilMonday = (currentDayOfWeek.value - 1 + 7) % 7
        val monday = today.minusDays(daysUntilMonday.toLong())

        val pendingDays = mutableListOf<LocalDate>()

        for (i in 0 until 7) {
            pendingDays.add(monday.plusDays(i.toLong()))
        }

        viewModelScope.launch {
            val sums = mutableListOf<Pair<String, Int>>()
            for (day in pendingDays) {
                val records = getRecordsForDay(day)
                val sumForDay = records.sumOf { it["amount"] as Int }
                sums.add("${day.dayOfWeek.toString()[0]}${day.dayOfWeek.toString()[1]}${day.dayOfWeek.toString()[2]}" to sumForDay)
            }
            _lastWeekSums.value = sums
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getRecordsForDay(day: LocalDate): List<Map<String, Any>> {
        val list = mutableListOf<Map<String, Any>>()
        return suspendCancellableCoroutine { continuation ->
            db.collection(uid)
                .whereEqualTo("year", day.year)
                .whereEqualTo("monthValue", day.monthValue)
                .whereEqualTo("dayOfMonth", day.dayOfMonth)
                .whereEqualTo("isSum", false)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        for (doc in snapshot.documents) {
                            list.add(
                                mapOf(
                                    "amount" to doc.data?.get("amount").toString().toInt(),
                                    "dayOfWeek" to doc.data?.get("dayOfWeek").toString(),
                                    "month" to doc.data?.get("month").toString(),
                                    "hour" to doc.data?.get("hour").toString().toInt(),
                                    "year" to doc.data?.get("year").toString().toInt(),
                                    "dayOfMonth" to doc.data?.get("dayOfMonth").toString().toInt(),
                                    "dayOfYear" to doc.data?.get("dayOfYear").toString().toInt(),
                                    "monthValue" to doc.data?.get("monthValue").toString().toInt(),
                                    "minute" to doc.data?.get("minute").toString().toInt(),
                                    "second" to doc.data?.get("second").toString().toInt(),
                                )
                            )
                        }
                        continuation.resume(list, null)
                    } else {
                        continuation.resume(emptyList(), null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching records for day $day", e)
                    continuation.resume(emptyList(), null)
                }
        }
    }

    fun listenForUserListOfRecordsAccMonths(year: Int, monthValue: Int) {

        db.collection(uid)
            .whereEqualTo("year", year)
            .whereEqualTo("monthValue", monthValue)
            .whereEqualTo("isSum", true)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Listen failed.", e)
                    _monthRecords.value = emptyList()
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = mutableListOf<Map<String, Any>>()
                    for (doc in snapshot.documents) {
                        doc.data?.let { list.add(it) }
                    }
                    val sortedList = list.sortedBy {
                        it.get("dayOfMonth").toString().toInt()
                    }
                    var monthRecords = mutableListOf<Map<String, Any>>()
                    for (i in 1..31) {
                        if (sortedList.isNotEmpty()) {
                            monthRecords.add(sortedList.checkDay(i) {
                                LocalDate.of(year, monthValue, i).toRecordMap()
                            })
                        }
                    }
                    val firstDayOfMonth = LocalDate.of(year, monthValue, 1).toRecordMap()
                    if (firstDayOfMonth.get("dayOfWeek") != "MONDAY") {
                        val firstDayNumber =
                            weekDayToInt(firstDayOfMonth.get("dayOfWeek").toString())
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
                    _monthRecords.value = monthRecords
                }
            }

    }
}