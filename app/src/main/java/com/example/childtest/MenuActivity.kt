package com.example.childtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.childtest.databinding.ActivityMenuBinding
//test

class MenuActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.numberTest.setOnClickListener(this)
        binding.setting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.number_test -> {
                val intent =
                    Intent(this@MenuActivity, TextToSpeechActivity::class.java)
                startActivity(intent)
            }
            R.id.setting -> {
                val intent =
                    Intent(this@MenuActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}