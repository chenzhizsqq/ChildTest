package com.example.childtest.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.childtest.R
import com.example.childtest.databinding.ActivityClockBinding
import java.util.*


class ClockActivity : BaseActivity() , View.OnClickListener , TimePicker.OnTimeChangedListener {
    private lateinit var binding: ActivityClockBinding
    val TAG = "ClockActivity"



    var thisRandomTime = ""
    var thisTouchTime = ""
    var bCheakRight = false

    private val digitalViewModel: DigitalViewModel by lazy {
        ViewModelProvider(this).get(DigitalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.timePicker1.setIs24HourView(false)
        binding.timePicker1.setOnTimeChangedListener(this)
        binding.next.setOnClickListener (this)
        binding.check.setOnClickListener (this)

        //让分数添加监视
        digitalViewModel.fenShu.observe(this, {
            binding.tvFenShu.text = "分数：$it"
        })
        digitalViewModel.fenShu.postValue(0)

        makeNewTest()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.next -> {
                binding.check.visibility = View.VISIBLE
                binding.next.visibility = View.GONE
                binding.timePicker1.visibility = View.VISIBLE
                binding.tvTestTime.visibility = View.VISIBLE
                makeNewTest()
            }
            R.id.check -> {
                if (thisRandomTime == thisTouchTime){
                    answerRight()
                }else{
                    answerWrong()
                }
            }
        }
    }

    private fun answerWrong() {
        if (digitalViewModel.getFenShu() > 0) {
            digitalViewModel.fenShuAdd(-1)
        }
        Toast(this).showCustomToast("$thisTouchTime : ❌", this)
    }

    private fun answerRight() {
        binding.check.visibility = View.GONE
        binding.next.visibility = View.VISIBLE
        binding.timePicker1.visibility = View.GONE
        binding.tvTestTime.visibility = View.GONE
        digitalViewModel.fenShuAdd(1)
        Toast(this).showCustomToast("$thisTouchTime : ◯", this)
    }


    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {

        var mHourOfDay = hourOfDay % 12

        var mMinute = ""

        if (minute>9){
            mMinute = minute.toString()
        }else{
            mMinute = "0$minute"
        }

        val str = String.format(Locale.US, "%d:%s", mHourOfDay, mMinute)

        Log.e("TAG", "setOnTimeChangedListener: "+str )

        thisTouchTime = str

        if (thisRandomTime == thisTouchTime){
            answerRight()
        }
    }

    private fun makeNewTest() {
        binding.check.visibility = View.VISIBLE
        binding.next.visibility = View.GONE
        binding.timePicker1.minute = 0
        binding.timePicker1.hour = 0
        bCheakRight = false
        thisRandomTime = randomTime()
        binding.tvTestTime.text = thisRandomTime
    }

    //"11:10"   11:08   1:02
    private fun randomTime(): String {

        val hourOfDay: Int = (Math.random() * 12).toInt()
        val minute: Int = (Math.random() * 60).toInt()

        val mHourOfDay = hourOfDay % 12

        val mMinute = if (minute > 9) {
            minute.toString()
        } else {
            "0$minute"
        }

        return String.format(Locale.US, "%d:%s", mHourOfDay, mMinute)

    }
}