package com.example.childtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * app给用户的设置，而创建的ViewModel
 */
class AppViewModel: ViewModel() {

    //共有的设置
    //"点击下一题后，马上读"
    val next_question_read = MutableLiveData(false)
    //随机
    val random_select = MutableLiveData(false)
    //是否显示提示
    val tips_is_show = MutableLiveData(false)
}