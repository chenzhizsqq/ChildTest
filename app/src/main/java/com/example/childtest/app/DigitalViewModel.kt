package com.example.childtest.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DigitalViewModel : ViewModel() {

    //当前的分数
    val fenShu = MutableLiveData(0)

    fun fenShuAdd(i: Int) {
        fenShu.value = fenShu.value!! + i
    }

    fun getFenShu(): Int {
        return fenShu.value!!
    }

    fun setFenShu(fenshu: Int) {
        fenShu.value = fenshu
    }

    //考试的数字倍数
    private val _beiShu = MutableLiveData(1)
    val beiShu: LiveData<Int> = _beiShu

    fun setBeiShu(beiShu: Int) {
        _beiShu.value = beiShu
    }

    fun getBeiShu(): Int {
        return _beiShu.value!!
    }

    //题目的前数
    private val _theFirstNumber = MutableLiveData(0)
    val theFirstNumber: LiveData<Int> = _theFirstNumber

    fun setFirstNumber(number: Int) {
        _theFirstNumber.value = number
    }


    //题目的后数
    private val _theLastNumber = MutableLiveData(0)
    val theLastNumber: LiveData<Int> = _theLastNumber

    fun setLastNumber(number: Int) {
        _theLastNumber.value = number
    }


    //答案
    private val _theAnswer = MutableLiveData(0)
    val theAnswer: LiveData<Int> = _theAnswer

    fun setAnswer(number: Int) {
        _theAnswer.value = number
    }

}

