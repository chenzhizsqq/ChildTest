package com.example.childtest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

//数据库各种操作
@Dao
interface MathScoreDao {
    @Query("SELECT * FROM MathScore order by dateTime desc")
    fun getAll(): List<MathScore>

    @Query("SELECT * FROM MathScore order by dateTime desc")
    fun getAllLive(): LiveData<List<MathScore>>

    @Query("SELECT * FROM MathScore order by dateTime desc")
    fun getAllFlow(): Flow<List<MathScore>>

    @Query("SELECT * FROM MathScore where date = :date order by dateTime desc")
    fun getAllByDate(date: String): List<MathScore>

    @Query("SELECT MAX(score) FROM MathScore where date = :date and id = :id order by dateTime desc")
    fun getMaxScore(date: String, id: String): Int

    @Query("SELECT MAX(score) FROM MathScore where date = :date and id = :id order by dateTime desc")
    fun getMaxScoreLive(date: String, id: String): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg post: MathScore)

    @Update
    suspend fun updateMathScore(vararg users: MathScore)

    @Delete
    suspend fun delete(post: MathScore)

    @Query("delete FROM MathScore")
    suspend fun deleteAll()
}