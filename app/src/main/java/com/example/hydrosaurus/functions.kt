package com.example.hydrosaurus

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