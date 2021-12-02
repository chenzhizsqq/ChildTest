package com.example.childtest

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.example.childtest.databinding.ActivityDigitalBinding
import java.util.*


class DigitalActivity : BaseActivity(), TextToSpeech.OnInitListener, View.OnClickListener {
    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityDigitalBinding

    private val TAG = "TextToSpeechActivity"
    private var how_much = "多少"
    private var currentAnswer = 0

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


        binding.llText.setOnClickListener(this)
        binding.nextTest.setOnClickListener(this)

        binding.answer1.setOnClickListener(this)
        binding.answer2.setOnClickListener(this)
        binding.answer3.setOnClickListener(this)

        initNumber()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nextTest -> {
                initNumber()
            }
            R.id.ll_text -> {
                this.tts!!.speak(
                    addTestFun(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "utteranceId"
                )
            }
            R.id.answer_1 -> {
                if (binding.answer1.text.toString() == currentAnswer.toString()) {
                    Toast(this).showCustomToast("✔", this)
                    speakMsg("答对了")
                    binding.nextTest.visibility = View.VISIBLE
                    binding.llAnswer.visibility = View.GONE
                } else {
                    Toast(this).showCustomToast("✖", this)
                    speakMsg("答错了")
                }
            }
            R.id.answer_2 -> {

                if (binding.answer2.text.toString() == currentAnswer.toString()) {
                    Toast(this).showCustomToast("✔", this)
                    speakMsg("答对了")
                    binding.nextTest.visibility = View.VISIBLE
                    binding.llAnswer.visibility = View.GONE
                } else {
                    Toast(this).showCustomToast("✖", this)
                    speakMsg("答错了")
                }
            }
            R.id.answer_3 -> {

                if (binding.answer3.text.toString() == currentAnswer.toString()) {
                    Toast(this).showCustomToast("✔", this)
                    speakMsg("答对了")
                    binding.nextTest.visibility = View.VISIBLE
                    binding.llAnswer.visibility = View.GONE
                } else {
                    Toast(this).showCustomToast("✖", this)
                    speakMsg("答错了")
                }
            }
        }
    }

    private fun initNumber() {
        //app:key="digital_preferences_max_num"的处理  数字的最大数
        val randomMax = ThisApp.sharedPreferences.getInt("digital_preferences_max_num", 3)
        var randomNum1 = Tools.randomNum(1, randomMax)
        var randomNum2 = Tools.randomNum(1, randomMax)
        while (
            binding.number1.text == randomNum1.toString() ||
            binding.number2.text == randomNum2.toString()
        ) {
            randomNum1 = Tools.randomNum(1, randomMax)
            randomNum2 = Tools.randomNum(1, randomMax)
        }
        binding.number1.text = randomNum1.toString()
        binding.number2.text = randomNum2.toString()


        binding.tvNum1.text = addViewText(randomNum1)
        binding.tvNum2.text = addViewText(randomNum2)

        currentAnswer = randomNum1 + randomNum2

        var answer2 = currentAnswer + Tools.randomNum(-2, 2)
        while (currentAnswer == answer2) {
            answer2 = currentAnswer + Tools.randomNum(-2, 2)
        }

        var answer3 = currentAnswer + Tools.randomNum(1, 3)
        while (currentAnswer == answer3
            || answer2 == answer3
        ) {
            answer3 = currentAnswer + Tools.randomNum(1, 3)
        }

        val answerList = listOf(answer2, currentAnswer, answer3)

        val startInt = Tools.randomNum(0, 2)

        val answers_random1 = answerList[startInt]
        val answers_random2 = answerList[(startInt + 1) % 3]
        val answers_random3 = answerList[(startInt + 2) % 3]

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


        //app:key="digital_preferences_tips_is_show"的处理  是否显示提示
        val tips_is_show =
            ThisApp.sharedPreferences.getBoolean("digital_preferences_tips_is_show", true)
        if (tips_is_show) {
            binding.llTips.visibility = View.VISIBLE
        } else {
            binding.llTips.visibility = View.GONE
        }

        binding.nextTest.visibility = View.GONE
        binding.llAnswer.visibility = View.VISIBLE


    }

    private fun speakMsg(s: String) {
        this.tts!!.speak(
            s,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "utteranceId"
        )
    }

    private fun addViewText(randomNum: Int): String {
        var r = ""
        for (i in 0 until randomNum) {
            r += "〇"
        }
        return r
    }

    private fun addTestFun(): String {
        val number1Text: String = binding.number1.text.toString()
        val number2Text: String = binding.number2.text.toString()
        return number1Text + "加$number2Text=" + how_much
    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // ロケールの指定
            val locale = Locale.CHINA
            if (this.tts!!.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                //tts!!.language = Locale.CHINA

                val chineseSpeak = ThisApp.sharedPreferences.getString("chineseSpeak", "")
                tts!!.language = Locale("zh", chineseSpeak)

                if (chineseSpeak == "HK") {
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

