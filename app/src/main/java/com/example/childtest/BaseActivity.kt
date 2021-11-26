package com.example.childtest

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    val TAG_BaseActivity = "BaseActivity"


    private val bTimeAble: Boolean by lazy {
        Tools.sharedPreGetBoolean("time_limit")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Log.e(TAG_BaseActivity, "onCreate: ")
        if (bTimeAble) {

            val timeSetting_ss = Tools.sharedPreGetInt("lock_use_time") * 60
            //Log.e(TAG_BaseActivity, "onCreate: timeSetting_ss:$timeSetting_ss")

            val remainingTime_ss = Tools.sharedPreGetInt(Config.remaining_time_ss)
            //Log.e(TAG_BaseActivity, "onCreate: remainingTime_ss:$remainingTime_ss")

            if (timeSetting_ss < remainingTime_ss) {
                countDownTimerMM(timeSetting_ss.toLong())
            } else {
                countDownTimerMM(remainingTime_ss.toLong())
            }
        }
    }

    private lateinit var countDownTimer: CountDownTimer

    //最后剩余时间 秒
    private var lastRemainingTime_ss: Int = 0

    fun countDownTimerMM(ss: Long) {
        countDownTimer = object : CountDownTimer(ss * 1000, 300) {
            override fun onFinish() {
                if (bTimeAble) {
                    Log.e(TAG_BaseActivity, "countDownTimerSS onFinish(): ")

                    Tools.sharedPrePut(Config.remaining_time_ss, 0)

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
            Tools.sharedPrePut(Config.remaining_time_ss, lastRemainingTime_ss)
            Log.d(
                TAG_BaseActivity,
                "onDestroy: remaining_time" + Tools.sharedPreGetInt(Config.remaining_time_ss)
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}