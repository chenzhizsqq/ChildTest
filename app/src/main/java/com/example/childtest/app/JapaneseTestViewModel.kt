package com.example.childtest.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JapaneseTestViewModel : ViewModel() {

    //当前的分数
    val fenShu = MutableLiveData(0)

}

