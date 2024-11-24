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