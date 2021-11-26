package com.example.childtest

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class ThisApp : Application() {
    val TAG = "ThisApp"

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //countDownTimer.cancel()
    }
}