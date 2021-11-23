package com.example.childtest

import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityJapaneseBinding
import com.example.childtest.databinding.ActivityJapaneseKataBinding
import java.util.*

class JapaneseKaTaActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityJapaneseKataBinding

    private lateinit var sharedPreferences: SharedPreferences

    private val bClickedRead: Boolean? by lazy {
        sharedPreferences.getBoolean("clicked_read", false)
    }
    private val bRandomSelect: Boolean? by lazy {
        sharedPreferences.getBoolean("random_select", true)
    }

    private val TAG = "JapaneseKaTaActivity"

    var numberText = ""

    val otherStrings = arrayOf(
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
        //this.supportActionBar?.hide()

        binding = ActivityJapaneseKataBinding.inflate(layoutInflater)
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

        binding.number.setOnClickListener { // 执行朗读
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