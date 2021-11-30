package com.example.childtest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.childtest.databinding.ActivityMenuBinding
import java.util.*

class MenuActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = "MenuActivity"
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.numberTest.setOnClickListener(this)
        binding.digitalTest.setOnClickListener(this)
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
            R.id.digital_test -> {
                val intent =
                    Intent(this@MenuActivity, DigitalActivity::class.java)
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
                val mSettingLogoDialog = SettingLogoDialog()
                mSettingLogoDialog.apply {
                    show(supportFragmentManager, "SettingLogoDialog")
                }
                mSettingLogoDialog.setOnDialogListener(object : SettingLogoDialog.OnDialogListener {
                    override fun onClick(bOpen: Boolean) {
                        if (bOpen) {
                            val intent =
                                Intent(this@MenuActivity, SettingsActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "错了，请再输入一次", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })

/*                val intent =
                    Intent(this@MenuActivity, SettingsActivity::class.java)
                startActivity(intent)*/
            }
        }
    }
}