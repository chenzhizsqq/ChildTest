package com.example.childtest.app

import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.example.childtest.appConfig.ThisApp
import com.example.childtest.databinding.ActivityEnglishBinding
import java.util.*

class EnglishActivity : BaseActivity(), TextToSpeech.OnInitListener {
    val TAG = "EnglishActivity"

    private lateinit var binding: ActivityEnglishBinding

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

        numberText = if (ThisApp.mAppViewModel.random_select.value == true) {
            testStrArray.random()
        } else {
            testStrArray[0]
        }
        binding.number.text = numberText

        binding.number.setOnClickListener {
            this.tts.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }


        binding.nextTest.setOnClickListener {
            if (ThisApp.mAppViewModel.random_select.value == true) {
                numberText = testStrArray.random()
            } else {
                arrayIndex += 1
                if (arrayIndex >= testStrArray.size) arrayIndex = 0

                numberText = testStrArray[arrayIndex]
            }

            binding.number.text = numberText
            if (ThisApp.mAppViewModel.next_question_read.value == true) {
                this.tts.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            }
        }


    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // ロケールの指定
            val locale = Locale.ENGLISH
            if (this.tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.language = Locale.ENGLISH

            }


            if (ThisApp.mAppViewModel.next_question_read.value == true) {
                // 音声合成の実行
                this.tts.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            }
        }
    }
}