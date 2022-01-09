package com.example.childtest.app

import android.os.Bundle
import android.view.View
import com.example.childtest.databinding.ActivityClockBinding

class ClockActivity : BaseActivity(),  View.OnClickListener {
    private lateinit var binding: ActivityClockBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onClick(v: View?) {
    }
}