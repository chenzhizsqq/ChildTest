package com.example.childtest

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

open class BaseActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener //共享属性的，属性变更的监听
{
    val TAG_BaseActivity = "BaseActivity"


    private val bTimeAble: Boolean by lazy {
        ThisApp.sharedPreferences.getBoolean("time_limit", false)
    }

    //点击后，马上读
    open var bClickedRead: Boolean = false

    //随机
    open var bRandomSelect: Boolean = false

    //https://www.geeksforgeeks.org/how-to-add-a-custom-styled-toast-in-android-using-kotlin/
    fun Toast.showCustomToast(message: String, activity: Activity) {
        val layout = activity.layoutInflater.inflate(
            R.layout.custom_toast_layout,
            activity.findViewById(R.id.toast_container)
        )

        // set the text of the TextView of the message
        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = message

        // use the application extension function
        this.apply {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bClickedRead = ThisApp.sharedPreferences.getBoolean("clicked_read", false)
        bRandomSelect = ThisApp.sharedPreferences.getBoolean("random_select", false)

        //Log.e(TAG_BaseActivity, "onCreate: ")
        if (bTimeAble) {

            val timeSetting_ss = ThisApp.sharedPreferences.getInt("lock_use_time", 30) * 60
            //Log.e(TAG_BaseActivity, "onCreate: timeSetting_ss:$timeSetting_ss")

            val remainingTime_ss =
                ThisApp.sharedPreferences.getInt(Config.remaining_time_ss, 30 * 60)
            //Log.e(TAG_BaseActivity, "onCreate: remainingTime_ss:$remainingTime_ss")

            if (timeSetting_ss < remainingTime_ss) {
                countDownTimerMM(timeSetting_ss.toLong())
            } else {
                countDownTimerMM(remainingTime_ss.toLong())
            }
        }

        //共享属性，追加属性变化的监听
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    //引用右上角的功能选择器
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        menu.findItem(R.id.clicked_read).isChecked = ThisApp.sharedPreGetBoolean("clicked_read")
        menu.findItem(R.id.random_select).isChecked = ThisApp.sharedPreGetBoolean("random_select")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clicked_read -> {
                item.isChecked = !item.isChecked
                ThisApp.sharedPrePut("clicked_read", item.isChecked)
                return true
            }
            R.id.random_select -> {
                item.isChecked = !item.isChecked
                ThisApp.sharedPrePut("random_select", item.isChecked)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var countDownTimer: CountDownTimer

    //最后剩余时间 秒
    private var lastRemainingTime_ss: Int = 0

    fun countDownTimerMM(ss: Long) {
        countDownTimer = object : CountDownTimer(ss * 1000, 300) {
            override fun onFinish() {
                if (bTimeAble) {
                    Log.e(TAG_BaseActivity, "countDownTimerSS onFinish(): ")

                    ThisApp.sharedPrePut(Config.remaining_time_ss, 0)

                    //倒数时间完了，退到LoginActivity页上。
                    val intent =
                        Intent(this@BaseActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if (bTimeAble) {

                    val hour = millisUntilFinished / 1000 / 60 / 60
                    val minute = millisUntilFinished / 1000 / 60 % 60
                    val second = millisUntilFinished / 1000 % 60
                    //Log.e(TAG_BaseActivity, "onTick: 倒计时"+hour+"小时"+minute+"分"+second+"秒", )
                    lastRemainingTime_ss = (millisUntilFinished / 1000).toInt()

                }

            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (bTimeAble) {
            countDownTimer.cancel()
            ThisApp.sharedPrePut(Config.remaining_time_ss, lastRemainingTime_ss)
            Log.d(
                TAG_BaseActivity,
                "onDestroy: remaining_time" + ThisApp.sharedPreGetInt(Config.remaining_time_ss)
            )
        }

        //共享属性，去除属性变化的监听
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    //共享属性，监听时的监听数据变化函数
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "clicked_read" -> {
                if (sharedPreferences != null) {
                    bClickedRead = sharedPreferences.getBoolean("clicked_read", false)
                }
            }
            "bRandomSelect" -> {
                if (sharedPreferences != null) {
                    bRandomSelect = sharedPreferences.getBoolean("random_select", false)
                }
            }

        }
    }

}