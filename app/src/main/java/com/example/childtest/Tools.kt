package com.example.childtest

import java.util.*

object Tools {
    private const val TAG = "Tools"
    fun randomNum(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }
}