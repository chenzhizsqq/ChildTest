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
         * 获取共享key的Boolean
         *
         * @param key
         */
        fun sharedPreGetBoolean(key: String): Boolean {
            return sharedPreferences.getBoolean(key, false)
        }

        fun sharedPreGetBoolean(key: String, defValue: Boolean): Boolean {
            return sharedPreferences.getBoolean(key, defValue)
        }

        /**
         * 获取共享key的String
         *
         * @param key
         */
        fun sharedPreGetString(key: String): String {
            return sharedPreferences.getString(key, "")!!
        }

        /**
         * 获取共享key的int
         *
         * @param key: String
         */
        fun sharedPreGetInt(key: String): Int {
            return sharedPreferences.getInt(key, 0)
        }

        /**
         * 获取共享key的Long
         *
         * @param key: String
         */
        fun sharedPreGetLong(key: String): Long {
            return sharedPreferences.getLong(key, 0)
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