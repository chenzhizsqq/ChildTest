package com.example.childtest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.childtest.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.numberTest.setOnClickListener(this)
        binding.japaneseTest.setOnClickListener(this)
        binding.japaneseKataTest.setOnClickListener(this)
        binding.englishTest.setOnClickListener(this)
        binding.setting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.number_test -> {
                val intent =
                    Intent(this@MenuActivity, TextToSpeechActivity::class.java)
                startActivity(intent)
            }
            R.id.japanese_test -> {
                val intent =
                    Intent(this@MenuActivity, JapaneseActivity::class.java)
                startActivity(intent)
            }
            R.id.japaneseKata_test -> {
                val intent =
                    Intent(this@MenuActivity, JapaneseKaTaActivity::class.java)
                startActivity(intent)
            }
            R.id.english_test -> {
                val intent =
                    Intent(this@MenuActivity, EnglishActivity::class.java)
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