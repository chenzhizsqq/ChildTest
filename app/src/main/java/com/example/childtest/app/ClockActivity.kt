package com.example.childtest.app

import android.os.Bundle
import android.util.Log
import com.example.childtest.databinding.ActivityClockBinding
import java.util.*


class ClockActivity : BaseActivity() {
    private lateinit var binding: ActivityClockBinding


    //"11:10"   11:08   1:02
    fun randomTime(): String {


        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timePicker1.setIs24HourView(false)
        binding.timePicker1.setOnTimeChangedListener { view, hourOfDay, minute ->


            var mHourOfDay = hourOfDay % 12
            if (mHourOfDay == 0 )mHourOfDay = 12

            var mMinute = ""

            if (minute>9){
                mMinute = minute.toString()
            }else{
                mMinute = "0$minute"
            }

            val str = String.format(Locale.US, "%d:%s", mHourOfDay, mMinute)

            binding.tvAddTime.text = str
            Log.e("TAG", "onCreate: "+str )
        }

    }


}