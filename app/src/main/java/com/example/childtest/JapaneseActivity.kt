package com.example.childtest

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityJapaneseBinding
import java.util.*

class JapaneseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityJapaneseBinding

    private lateinit var sharedPreferences: SharedPreferences

    private val bClickedRead: Boolean? by lazy {
        sharedPreferences.getBoolean("clicked_read",false)
    }

    private val TAG = "JapaneseActivity"

    var numberText = ""

    val otherStrings = arrayOf(
        "あ","い","う","え","お",
        "か","き","く","け","こ",
        "さ","し","す","せ","そ",
        "た","ち","つ","て","と",
        "な","に","ぬ","ね","の",
        "は","ひ","ふ","へ","ほ",
        "ま","み","む","め","も",
        "や","ゆ","よ",
        "ら","り","る","れ","ろ",
        "わ","を","ん")

    // 起動時に呼ばれる
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.supportActionBar?.hide()

        binding = ActivityJapaneseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)




        numberText = otherStrings.random()
        binding.number.text = numberText

        binding.number.setOnClickListener { // 执行朗读
            numberText = otherStrings.random()

            binding.number.text = numberText
            if (bClickedRead == true){
                this.tts!!.speak(numberText, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
            }

            Log.e(TAG, "onCreate: binding.number" )
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


            // 音声合成の実行
            this.tts!!.speak("開始します。", TextToSpeech.QUEUE_FLUSH, null, "utteranceId")

        }
    }
}