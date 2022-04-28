package com.example.childtest.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//数据库各种操作
@Dao
interface MathScoreDao {
    @Query("SELECT * FROM MathScore order by dateTime desc")
    fun getAll(): List<MathScore>

    @Query("SELECT * FROM MathScore order by dateTime desc")
    fun getAllFlow(): Flow<List<MathScore>>

    @Query("SELECT * FROM MathScore where date = :date order by dateTime desc")
    fun getAllByDate(date:String): List<MathScore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg post: MathScore)

    @Update
    suspend fun updateMathScore(vararg users: MathScore)

    @Delete
    suspend fun delete(post: MathScore)
}