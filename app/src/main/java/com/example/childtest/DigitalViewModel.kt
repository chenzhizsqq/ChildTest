package com.example.childtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DigitalViewModel : ViewModel() {

    //当前的分数
    private val _fenShu = MutableLiveData(0)
    val liveFenShu: LiveData<Int> = _fenShu
    fun fenShuAdd(i:Int){
        _fenShu.value = _fenShu.value!! + i
    }
    fun getFenShu(): Int {
        return _fenShu.value!!
    }

    //是否显示提示
    val mTipsIsShow = MutableLiveData(true)
    val liveTipsIsShow: LiveData<Boolean> = mTipsIsShow
}

