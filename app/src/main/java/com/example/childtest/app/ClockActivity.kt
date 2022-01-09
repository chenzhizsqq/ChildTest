package com.example.childtest.app

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import com.example.childtest.databinding.ActivityClockBinding
import java.util.*

class ClockActivity : BaseActivity(),  View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityClockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(v: View?) {
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        var mHourOfDay = hourOfDay % 12
        if (mHourOfDay == 0 )mHourOfDay = 12

        var mMinute = ""

        if (minute>10){
            mMinute = minute.toString()
        }else{
            mMinute = "0$minute"
        }

        val str = String.format(Locale.US, "%d:%s", mHourOfDay, mMinute)

        binding.tvAddTime.text = str
    }

    // タイマー設定ボタンをクリックした時のメソッド
    fun showTimePickerDialog(v: View) {
        //TImePickクラス呼び出し
        val newFragment = TimePick()
        newFragment.show(supportFragmentManager, "timePicker")

    }
}