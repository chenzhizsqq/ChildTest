package com.example.childtest.appConfig

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Tools {
    private const val TAG = "Tools"
    fun randomNum(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }

    fun getRandomBoolean(): Boolean {
        val random = Random()
        return random.nextBoolean()
    }

    fun getDate():String{
        val dt = ZonedDateTime.now()
        val formatterday = DateTimeFormatter.ofPattern("YYYYMMdd")
        val date = dt.format(formatterday)
        return date
    }


    fun getDateTime():String{
        val dt = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("YYYYMMddHHmmss")
        val datetime = dt.format(formatter)
        return datetime
    }

}