package com.example.childtest

import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityEnglishBinding
import java.util.*

class EnglishActivity : BaseActivity(), TextToSpeech.OnInitListener {
    val TAG = "JapaneseActivity"
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityEnglishBinding

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    private val bClickedRead: Boolean? by lazy {
        Tools.sharedPreGetBoolean("clicked_read")
    }
    private val bRandomSelect: Boolean? by lazy {
        sharedPreferences.getBoolean("random_select", true)
    }

    var numberText = ""

    private val testStrArray = arrayOf(
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

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)




        numberText = if (bRandomSelect == true) {
            testStrArray.random()
        } else {
            testStrArray[0]
        }
        binding.number.text = numberText

        binding.number.setOnClickListener {

            if (bRandomSelect == true) {
                numberText = testStrArray.random()
            } else {
                arrayIndex += 1
                if (arrayIndex >= testStrArray.size) arrayIndex = 0

                numberText = testStrArray[arrayIndex]
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