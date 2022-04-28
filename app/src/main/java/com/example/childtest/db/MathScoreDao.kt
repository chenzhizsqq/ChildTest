package com.example.childtest.db

import androidx.room.*

//数据库各种操作
@Dao
interface MathScoreDao {
    @Query("SELECT * FROM MathScore")
    fun getAll(): List<MathScore>

    @Insert
    fun insertAll(vararg post: MathScore)

    @Update
    fun updateMathScore(vararg users: MathScore)

    @Delete
    fun delete(post: MathScore)
}