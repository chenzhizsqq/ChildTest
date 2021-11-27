package com.example.childtest

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Size
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityDigitalBinding
import java.util.*
import android.widget.TextView




class DigitalActivity : BaseActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityDigitalBinding

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }
    private val bClickedRead: Boolean? by lazy {
        sharedPreferences.getBoolean("clicked_read", false)
    }

    private val TAG = "TextToSpeechActivity"
    private var how_much = "多少"

    // 起動時に呼ばれる
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDigitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)


        binding.llText.setOnClickListener { // 执行朗读

            this.tts!!.speak(
                addTestFun(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                "utteranceId"
            )
        }

        binding.nextTest.setOnClickListener {
            initNumber()
        }

        initNumber()
    }

    private fun initNumber() {
        val randomNum1 = Tools.randomNum(1, 3)
        val randomNum2 = Tools.randomNum(1, 3)
        binding.number1.text = randomNum1.toString()
        binding.number2.text = randomNum2.toString()

        //ll_image_1    ll_image_2
        binding.llImage1.removeAllViews()
        binding.llImage2.removeAllViews()

        for (i in 0 until randomNum1) {
            binding.llImage1.addView(createText(Color.RED))
        }
        for (i in 0 until randomNum2) {
            binding.llImage2.addView(createText(Color.BLUE))
        }

        if (bClickedRead == true) {
            this.tts!!.speak(
                addTestFun(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                "utteranceId"
            )
        }
    }

    private fun createText(color:Int):TextView {
        val textView1 = TextView(this)
        textView1.text = "〇"
        textView1.textSize = 40F
        textView1.setTextColor(color)
        return textView1
    }

    private fun addTestFun(): String {
        val number1Text: String = binding.number1.text.toString()
        val number2Text: String = binding.number2.text.toString()
        return number1Text +"加$number2Text=" + how_much
    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // ロケールの指定
            val locale = Locale.CHINA
            if (this.tts!!.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                //tts!!.language = Locale.CHINA

                val chineseSpeak = sharedPreferences.getString("chineseSpeak", "")
                tts!!.language = Locale("zh", chineseSpeak)

                if (chineseSpeak == "HK"){
                    how_much = "几多"
                }
            }


            if (bClickedRead == true) {
                initNumber()
            }
        }
    }
}