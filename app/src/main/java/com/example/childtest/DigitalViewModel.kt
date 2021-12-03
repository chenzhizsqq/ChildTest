package com.example.childtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DigitalViewModel : ViewModel() {

    //当前的分数
    val mFenShu = MutableLiveData(0)
    val liveFenShu: LiveData<Int> = mFenShu

    //是否显示提示
    val mTipsIsShow = MutableLiveData(true)
    val liveTipsIsShow: LiveData<Boolean> = mTipsIsShow
}

