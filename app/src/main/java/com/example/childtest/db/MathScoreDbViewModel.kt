package com.example.childtest.db

import android.content.Context
import androidx.lifecycle.ViewModel


class MathScoreDbViewModel(context: Context) : ViewModel() {

    private val mMathScoreDao: MathScoreDao by lazy {
        MathScoreDB.getInstance(context).mathScoreDao()
    }


    suspend fun insert(mathScore:MathScore){
        mMathScoreDao.insertAll(mathScore)
    }

    fun selectGetAll() : List<MathScore>{
        return  mMathScoreDao.getAll()
    }


}