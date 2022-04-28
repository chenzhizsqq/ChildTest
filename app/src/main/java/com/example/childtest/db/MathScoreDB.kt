package com.example.childtest.db

import androidx.room.Database
import androidx.room.RoomDatabase


//数据库的接口
@Database(entities = [MathScore::class], version = 1)
abstract class MathScoreDB : RoomDatabase() {
    abstract fun mathScoreDao(): MathScoreDao
}