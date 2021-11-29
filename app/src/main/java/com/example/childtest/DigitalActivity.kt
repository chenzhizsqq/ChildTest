package com.example.childtest

import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityDigitalBinding
import java.util.*
import android.widget.TextView
import androidx.preference.PreferenceFragmentCompat


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


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.digital_settings, DigitalSettingsFragment())
                .commit()
        }

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
        var randomMax = sharedPreferences.getInt("digital_preferences_max_num", 3)
        var randomNum1 = Tools.randomNum(1, randomMax)
        var randomNum2 = Tools.randomNum(1, randomMax)
        while (
            binding.number1.text == randomNum1.toString() ||
            binding.number2.text == randomNum2.toString()
        ){
            randomNum1 = Tools.randomNum(1, randomMax)
            randomNum2 = Tools.randomNum(1, randomMax)
        }
        binding.number1.text = randomNum1.toString()
        binding.number2.text = randomNum2.toString()

        //ll_image_1    ll_image_2
/*        binding.llImage1.removeAllViews()
        binding.llImage2.removeAllViews()*/

/*        for (i in 0 until randomNum1) {
            binding.llImage1.addView(createText(Color.RED))
        }
        for (i in 0 until randomNum2) {
            binding.llImage2.addView(createText(Color.BLUE))
        }*/


        binding.tvNum1.text = addViewText(randomNum1)
        binding.tvNum2.text = addViewText(randomNum2)

        val answer = randomNum1 + randomNum2

        var answer2 = answer + Tools.randomNum(-2, 2)
        while (answer == answer2){
            answer2 = answer + Tools.randomNum(-2, 2)
        }

        var answer3 = answer + Tools.randomNum(1, 3)
        while (answer == answer3
            || answer2 == answer3){
            answer3 = answer + Tools.randomNum(1, 3)
        }

        val answerList = listOf(answer2 , answer ,answer3)

        val startInt = Tools.randomNum(0,2)

        val answers_random1 = answerList[startInt]
        val answers_random2 = answerList[(startInt+1)%3]
        val answers_random3 = answerList[(startInt+2)%3]

        binding.answer1.text = answers_random1.toString()
        binding.answer2.text = answers_random2.toString()
        binding.answer3.text = answers_random3.toString()

        if (bClickedRead == true) {
            this.tts!!.speak(
                addTestFun(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                "utteranceId"
            )
        }
    }

    private fun addViewText(randomNum: Int):String {
        var r = ""
        for (i in 0 until randomNum) {
            r += "〇"
        }
        return r
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
    class DigitalSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.digital_preferences, rootKey)
        }
    }
}

