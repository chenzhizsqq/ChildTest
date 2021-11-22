package com.example.childtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.example.childtest.databinding.ActivityTextToSpeechBinding
import java.util.*

class TextToSpeechActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityTextToSpeechBinding

    private var randomInt = 0

    // 起動時に呼ばれる
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.supportActionBar?.hide()

        binding = ActivityTextToSpeechBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)

        binding.number.text = randomInt.toString()

        binding.number.setOnClickListener { // 执行朗读
            val min = 0
            val max = 100
            randomInt = Random().nextInt(max - min + 1) + min

            binding.number.text = randomInt.toString()
            this.tts!!.speak(randomInt.toString(), TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }

        binding.numberSpeak.setOnClickListener {
            this.tts!!.speak(randomInt.toString(), TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
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
            val locale = Locale.CHINA
            if (this.tts!!.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts!!.language = Locale.CHINA
            }

            binding.number.text = "开始"

            // 音声合成の実行
            this.tts!!.speak("开始", TextToSpeech.QUEUE_FLUSH, null, "utteranceId")

        }
    }
}