package com.example.childtest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//数据库的接口
@Database(entities = [MathScore::class], version = 1)
abstract class MathScoreDB : RoomDatabase() {
    abstract fun mathScoreDao(): MathScoreDao


    companion object {
        private const val DATABASE_NAME = "Posts_db"
        private lateinit var mMathScoreDB: MathScoreDB

        //注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。
        //每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例
        fun getInstance(context: Context): MathScoreDB {
            if (!this::mMathScoreDB.isInitialized) {
                //创建的数据库的实例
                mMathScoreDB = Room.databaseBuilder(
                    context.applicationContext,
                    MathScoreDB::class.java,
                    DATABASE_NAME
                ).build()
            }
            return mMathScoreDB
        }

    }
}