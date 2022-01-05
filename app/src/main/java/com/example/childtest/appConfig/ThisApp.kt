package com.example.childtest.appConfig

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

        val mAppViewModel = AppViewModel()

        /**
         * 提交共享key的String
         *
         * @param key:String
         * @param value:String
         */
        fun sharedPrePut(key: String, value: String) {
            val editor = sharedPreferences.edit()
            editor.apply {
                putString(key, value)
            }.apply()
        }

        /**
         * 提交共享key的int
         *
         * @param key:String
         * @param value:Int
         */
        fun sharedPrePut(key: String, value: Int) {
            val editor = sharedPreferences.edit()
            editor.apply {
                putInt(key, value)
            }.apply()
        }

        /**
         * 提交共享key的Boolean
         *
         * @param key:String
         * @param value:Boolean
         */
        fun sharedPrePut(key: String, value: Boolean) {
            val editor = sharedPreferences.edit()
            editor.apply {
                putBoolean(key, value)
            }.apply()
        }


        /**
         * 提交共享key的Long
         *
         * @param key:String
         * @param value:Long
         */
        fun sharedPrePut(key: String, value: Long) {
            val editor = sharedPreferences.edit()
            editor.apply {
                putLong(key, value)
            }.apply()
        }

        /**
         * 删除共享key
         *
         * @param key
         */
        fun sharedPreRemove(key: String) {
            val editor = sharedPreferences.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //countDownTimer.cancel()
    }
}