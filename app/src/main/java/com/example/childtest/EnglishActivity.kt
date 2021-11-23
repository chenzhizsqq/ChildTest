package com.example.childtest

import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityEnglishBinding
import java.util.*

class EnglishActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityEnglishBinding

    private lateinit var sharedPreferences: SharedPreferences

    private val bClickedRead: Boolean? by lazy {
        sharedPreferences.getBoolean("clicked_read", false)
    }
    private val bRandomSelect: Boolean? by lazy {
        sharedPreferences.getBoolean("random_select", true)
    }


    private val TAG = "JapaneseActivity"

    var numberText = ""

    val otherStrings = arrayOf(
        "A", "B", "C", "D", "E",
        "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y",
        "Z"
    )
    var arrayIndex = 0

    // 起動時に呼ばれる
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.supportActionBar?.hide()

        binding = ActivityEnglishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)




        if (bRandomSelect == true) {
            numberText = otherStrings.random()
        } else {
            numberText = otherStrings[0]
        }
        binding.number.text = numberText

        binding.number.setOnClickListener {

            if (bRandomSelect == true) {
                numberText = otherStrings.random()
            } else {
                arrayIndex += 1
                if (arrayIndex >= otherStrings.size) arrayIndex = 0

                numberText = otherStrings[arrayIndex]
            }

            binding.number.text = numberText
            if (bClickedRead == true) {
                this.tts!!.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            }
        }


        binding.numberSpeak.setOnClickListener {
            this.tts!!.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // ロケールの指定
            val locale = Locale.ENGLISH
            if (this.tts!!.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts!!.language = Locale.ENGLISH

            }


            if (bClickedRead == true) {
                // 音声合成の実行
                this.tts!!.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            }
        }
    }
}