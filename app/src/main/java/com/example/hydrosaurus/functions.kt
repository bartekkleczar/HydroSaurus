package com.example.hydrosaurus

import java.time.LocalDate

fun String.containsChar(x: Char): Boolean{
    for (i in this){
        if(i == x) return true
    }
    return false
}

fun String.checkIfAbleToFloat(): Int{
    return try {
        (this.toFloat()* 1000).toInt()
    } catch (e: Exception){
        0
    }
}

fun Any?.minutesCorrection(): String{
    return if (this.toString().length == 1){
        "${this}0"
    }
    else this.toString()
}

fun String.safeToFloat(): Float{
    return try {
        (this.toFloat())
    } catch (e: Exception){
        0f
    }
}

fun LocalDate.toRecordMap(): Map<String, Any>{
    return mapOf(
        "amount" to 0,
        "dayOfWeek" to this.dayOfWeek.toString(),
        "month" to this.month.toString(),
        "hour" to 0,
        "year" to this.year.toString().toInt(),
        "dayOfMonth" to this.dayOfMonth.toString().toInt(),
        "dayOfYear" to this.dayOfYear.toString().toInt(),
        "monthValue" to this.monthValue.toString().toInt(),
        "minute" to 0,
        "second" to 0,
    )
}

fun List<Map<String, Any>>.checkDay(dayOfMonth: Int, emptyDay: () -> Map<String, Any>): Map<String, Any> {
    for(i in this){
        if(i["dayOfMonth"].toString().toInt() == dayOfMonth){
            return i
        }
    }
    return emptyDay()
}