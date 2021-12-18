package com.example.childtest.app

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

}

