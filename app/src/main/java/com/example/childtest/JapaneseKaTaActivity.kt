package com.example.childtest

import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.example.childtest.databinding.ActivityJapaneseKataBinding
import java.util.*

class JapaneseKaTaActivity : BaseActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityJapaneseKataBinding

    private val TAG = "JapaneseKaTaActivity"

    var numberText = ""

    private val testStrArray = arrayOf(
        "ア", "イ", "ウ", "エ", "オ",
        "カ", "キ", "ク", "ケ", "コ",
        "サ", "シ", "ス", "セ", "ソ",
        "タ", "チ", "ツ", "テ", "ト",
        "ナ", "ニ", "ヌ", "ネ", "ノ",
        "ハ", "ヒ", "フ", "ヘ", "ホ",
        "マ", "ミ", "ム", "メ", "モ",
        "ヤ", "ユ", "ヨ",
        "ラ", "リ", "ル", "レ", "ロ",
        "ワ", "オ", "ン"
    )
    var arrayIndex = 0

    // 起動時に呼ばれる
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJapaneseKataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)




        if (bRandomSelect == true) {
            numberText = testStrArray.random()
        } else {
            numberText = testStrArray[0]
        }
        binding.number.text = numberText

        binding.number.setOnClickListener { // 执行朗读
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

/*    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }*/

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // ロケールの指定
            val locale = Locale.JAPAN
            if (this.tts!!.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts!!.language = Locale.JAPAN

            }


            if (bClickedRead == true) {
                // 音声合成の実行
                this.tts!!.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            }

        }
    }
}