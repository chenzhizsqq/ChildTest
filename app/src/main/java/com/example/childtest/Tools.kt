package com.example.childtest

import java.util.*

object Tools {
    private const val TAG = "Tools"
    fun randomNum(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }


    /**
     * 提交共享key的String
     *
     * @param key:String
     * @param value:String
     */
    fun sharedPrePut(key: String, value: String) {
        val editor = ThisApp.sharedPreferences.edit()
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
        val editor = ThisApp.sharedPreferences.edit()
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
        val editor = ThisApp.sharedPreferences.edit()
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
        return ThisApp.sharedPreferences.getBoolean(key, false)
    }

    /**
     * 获取共享key的String
     *
     * @param key
     */
    fun sharedPreGetString(key: String): String {
        return ThisApp.sharedPreferences.getString(key, "")!!
    }

    /**
     * 获取共享key的int
     *
     * @param key: String
     */
    fun sharedPreGetInt(key: String): Int {
        return ThisApp.sharedPreferences.getInt(key, 0)
    }

    /**
     * 获取共享key的Long
     *
     * @param key: String
     */
    fun sharedPreGetLong(key: String): Long {
        return ThisApp.sharedPreferences.getLong(key, 0)
    }

    /**
     * 删除共享key
     *
     * @param key
     */
    fun sharedPreRemove(key: String) {
        val editor = ThisApp.sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}