package com.example.childtest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//数据库的结构
@Entity(tableName = "MathScore")
data class MathScore(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "score") val score: Int?,
    @ColumnInfo(name = "dateTime") val dateTime: String?
)
