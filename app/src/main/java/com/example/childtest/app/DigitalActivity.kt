package com.example.childtest.app

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.childtest.R
import com.example.childtest.appConfig.ThisApp
import com.example.childtest.appConfig.Tools
import com.example.childtest.databinding.ActivityDigitalBinding
import java.util.*


class DigitalActivity : BaseActivity(), TextToSpeech.OnInitListener, View.OnClickListener {

    private lateinit var binding: ActivityDigitalBinding

    private val TAG = "DigitalActivity"
    private var plus = "加"
    private var currentAnswer = 0

    //是否不是加减法一起选择?
    val is_match_two =
        ThisApp.sharedPreferences.getBoolean("digital_preferences_match_two", true)

    //是否加减
    var is_add_match =
        ThisApp.sharedPreferences.getBoolean("digital_preferences_match_select", true)

    //是否中文
    val is_speak_chinese =
        ThisApp.sharedPreferences.getBoolean("digital_preferences_speak_chinese", true)

    //错了之后，是否马上下一题
    val is_after_wrong_is_next =
        ThisApp.sharedPreferences.getBoolean("digital_preferences_after_wrong_is_next", true)

    //错了后，扣多少分？
    val digital_preferences_deduct_points_for_mistakes =
        ThisApp.sharedPreferences.getInt("digital_preferences_deduct_points_for_mistakes", 1)

    private val digitalViewModel: DigitalViewModel by lazy {
        ViewModelProvider(this).get(DigitalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDigitalBinding.inflate(layoutInflater)
        setContentView(binding.root)


/*        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.digital_settings, DigitalSettingsFragment())
                .commit()
        }*/

        binding.llText.setOnClickListener(this)
        binding.nextTest.setOnClickListener(this)

        binding.answer1.setOnClickListener(this)
        binding.answer2.setOnClickListener(this)
        binding.answer3.setOnClickListener(this)


        //让分数添加监视
        digitalViewModel.fenShu.observe(this, {
            binding.test1.text = "分数：$it"
        })

        //是否提示添加监视
        ThisApp.mAppViewModel.tips_is_show.observe(this, {
            if (it) {
                binding.llTips.visibility = View.VISIBLE
            } else {
                binding.llTips.visibility = View.GONE
            }
        })

        // [初始化时隐藏 begin]
        binding.llText.visibility = View.GONE
        binding.llAnswer.visibility = View.GONE
        // [初始化时隐藏 end]
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nextTest -> {
                initNumber()
            }
            R.id.ll_text -> {
                this.tts.speak(
                    addTestFun(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "utteranceId"
                )
            }
            R.id.answer_1 -> {
                if (binding.answer1.text.toString() == currentAnswer.toString()) {
                    answerRight(binding.answer1.text.toString())
                } else {
                    answerWrong(binding.answer1.text.toString())
                }
            }
            R.id.answer_2 -> {

                if (binding.answer2.text.toString() == currentAnswer.toString()) {
                    answerRight(binding.answer2.text.toString())
                } else {
                    answerWrong(binding.answer2.text.toString())
                }
            }
            R.id.answer_3 -> {

                if (binding.answer3.text.toString() == currentAnswer.toString()) {
                    answerRight(binding.answer3.text.toString())
                } else {
                    answerWrong(binding.answer3.text.toString())
                }
            }
        }
    }

    private fun answerWrong(answer: String) {
        Toast(this).showCustomToast("$answer:❌", this)

        if (is_speak_chinese) {
            speakMsg("答错了。")
        } else {
            speakMsg("間違えます。")
        }

        digitalViewModel.fenShuAdd(-digital_preferences_deduct_points_for_mistakes)
        if (digitalViewModel.getFenShu() < 0) {
            digitalViewModel.setFenShu(0)
        }


        //错了之后，是否马上下一题
        if (is_after_wrong_is_next) {
            binding.nextTest.visibility = View.VISIBLE
            binding.llAnswer.visibility = View.GONE
        }
    }

    private fun answerRight(answer: String) {
        Toast(this).showCustomToast("$answer:◯", this)
        if (is_speak_chinese) {
            speakMsg("答对了。")
        } else {
            speakMsg("正解です。")
        }
        digitalViewModel.fenShuAdd(1)
        binding.nextTest.visibility = View.VISIBLE
        binding.llAnswer.visibility = View.GONE
    }

    private fun initNumber() {
        binding.llText.visibility = View.VISIBLE
        binding.llAnswer.visibility = View.VISIBLE
        if (!is_match_two) {
            is_add_match = Tools.getRandomBoolean()
        }
        if (is_add_match) {
            //加法
            plus = if (is_speak_chinese) {
                "加"
            } else {
                "たす"
            }
            binding.matchStyle.text = "+"

            //app:key="digital_preferences_max_num"的处理  数字的最大数
            //测试数字
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

            //选择答案设置
            currentAnswer = randomNum1 + randomNum2

            val answer2: Int = if (Tools.getRandomBoolean()) {
                currentAnswer + 1
            } else {
                currentAnswer - 1
            }

            val answer3: Int = if (Tools.getRandomBoolean()) {
                currentAnswer + 2
            } else {
                currentAnswer - 2
            }

            val answerList = listOf(answer2, currentAnswer, answer3)

            val startInt = Tools.randomNum(0, 2)

            val answers_random1 = answerList[startInt]
            val answers_random2 = answerList[(startInt + 1) % 3]
            val answers_random3 = answerList[(startInt + 2) % 3]

            binding.answer1.text = answers_random1.toString()
            binding.answer2.text = answers_random2.toString()
            binding.answer3.text = answers_random3.toString()

        } else {
            //减法

            plus = if (is_speak_chinese) {
                "减"
            } else {
                "ひく"
            }
            binding.matchStyle.text = "-"


            //app:key="digital_preferences_max_num"的处理  数字的最大数
            //测试数字
            val randomMax = ThisApp.sharedPreferences.getInt("digital_preferences_max_num", 3)
            var randomNum1 = Tools.randomNum(1, randomMax)
            var randomNum2 = Tools.randomNum(0, randomMax)

            //最后的答案
            var lastAnswer = currentAnswer

            //选择答案设置
            currentAnswer = randomNum1 - randomNum2
            while (
                randomNum1 <= randomNum2
                || binding.number1.text == randomNum1.toString()
                || binding.number2.text == randomNum2.toString()
                || randomNum1 - randomNum2 < 0
                || lastAnswer == currentAnswer
            ) {
                randomNum1 = Tools.randomNum(1, randomMax)
                randomNum2 = Tools.randomNum(0, randomMax)

                /* 为了少一点等于0 begin*/
                if (randomNum2 == 0) randomNum2 = Tools.randomNum(0, randomMax)
                if (randomNum2 == 0) randomNum2 = Tools.randomNum(0, randomMax)
                /* 为了少一点等于0 end*/

                currentAnswer = randomNum1 - randomNum2
            }
            binding.number1.text = randomNum1.toString()
            binding.number2.text = randomNum2.toString()


            binding.tvNum1.text = addViewText(randomNum1)
            binding.tvNum2.text = addViewText(randomNum2)


            var answer2: Int = 0
            answer2 = if (Tools.getRandomBoolean()) {
                currentAnswer + 1
            } else {
                currentAnswer - 1
            }
            if (answer2 < 0) answer2 = currentAnswer + 4

            var answer3: Int = 0
            answer3 = if (Tools.getRandomBoolean()) {
                currentAnswer + 2
            } else {
                currentAnswer + 3
            }

            val answerList = listOf(answer2, currentAnswer, answer3)

            val startInt = Tools.randomNum(0, 2)

            val answers_random1 = answerList[startInt]
            val answers_random2 = answerList[(startInt + 1) % 3]
            val answers_random3 = answerList[(startInt + 2) % 3]

            binding.answer1.text = answers_random1.toString()
            binding.answer2.text = answers_random2.toString()
            binding.answer3.text = answers_random3.toString()
        }


        if (ThisApp.mAppViewModel.next_question_read.value == true) {
            this.tts.speak(
                addTestFun(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                "utteranceId"
            )
        }

        binding.nextTest.visibility = View.GONE
        binding.llAnswer.visibility = View.VISIBLE
    }

    private fun speakMsg(s: String) {
        this.tts.speak(
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
        return if (is_speak_chinese) {
            "$number1Text $plus $number2Text 等于 几？"
        } else {
            "$number1Text $plus $number2Text は 何ですか？"
        }
        return ""
    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        super.onInit(status)

        // 音声合成の実行
        this.tts.speak("hello", TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }

/*    class DigitalSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.digital_preferences, rootKey)
        }
    }*/
}

