package com.example.childtest.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MathScoreDbViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: MathScoreDao

    init {
        val db = MathScoreDB.getInstance(application) // DBにアクセスするclassで一度だけDBをビルドする
        dao = db.mathScoreDao() // 使用するDaoを指定
    }


    fun insert(mathScore: MathScore) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertAll(mathScore)
        }
    }

    fun selectGetAll(): List<MathScore> {
        return dao.getAll()
    }

    fun getMaxScore(date: String, id: String): Int {
        return dao.getMaxScore(date, id)
    }

    fun getMaxScoreLive(date: String, id: String): LiveData<Int> {
        return dao.getMaxScoreLive(date, id)
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAll()
        }
    }


}