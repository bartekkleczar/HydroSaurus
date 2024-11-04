package com.example.hydrosaurus

fun String.contains(x: Char): Boolean{
    for (i in this){
        if(i == x) return true
    }
    return false
}