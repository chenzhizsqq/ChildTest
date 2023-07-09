package com.example.childtest.app

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.childtest.R
import com.example.childtest.appConfig.ThisApp
import com.example.childtest.appConfig.Tools
import com.example.childtest.databinding.ActivityDigitalBinding
import com.example.childtest.db.MathScore
import com.example.childtest.db.MathScoreDbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DigitalActivity : BaseActivity(), TextToSpeech.OnInitListener, View.OnClickListener {

    companion object {
        private const val TAG = "DigitalActivity"
    }

    private lateinit var binding: ActivityDigitalBinding

    private var plus = "加"

    //考试倒计时
    private lateinit var examCountdown: CountDownTimer


    //当前算法的答案
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

    //数学的倍数
    val multiple_of_Mathematics =
        ThisApp.sharedPreferences.getString("multiple_of_Mathematics", "1")

    //要固定第一个数为最大值吗？
    val is_set_first_num =
        ThisApp.sharedPreferences.getBoolean("digital_preferences_is_set_first_num", true)

    //digital_preferences_is_set_count_down 要设定倒数时间吗
    val is_set_count_down =
        ThisApp.sharedPreferences.getBoolean("digital_preferences_is_set_count_down", false)

    //digital_preferences_is_set_count_down_second  倒数时间
    val digital_preferences_is_set_count_down_second =
        ThisApp.sharedPreferences.getInt("digital_preferences_is_set_count_down_second", 10)

    private val digitalViewModel: DigitalViewModel by lazy {
        ViewModelProvider(this)[DigitalViewModel::class.java]
    }

    val mMathScoreDbViewModel: MathScoreDbViewModel by viewModels()


    private lateinit var toast: Toast

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

        //mMathScoreDbViewModel = MathScoreDbViewModel(this)

        binding.llText.setOnClickListener(this)
        binding.nextTest.setOnClickListener(this)

        binding.answer1.setOnClickListener(this)
        binding.answer2.setOnClickListener(this)
        binding.answer3.setOnClickListener(this)

        toast = Toast(this)


        //让分数添加监视
        digitalViewModel.fenShu.observe(this, {
            binding.test1.text = "分数：$it"

            //最后分数记录到数据库中。
            insertScore(it)
        })


        //今天最高分是。
        // Roomの変更を検知し、変更時にtextViewの内容を変更する
        mMathScoreDbViewModel.getMaxScoreLive(Tools.getDate(), TAG).observe(this, { value ->
            value?.let {
                binding.maxScore.text = "今天最高分：" + it.toString()
            }
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

        //倍数
        if (multiple_of_Mathematics != null) {
            digitalViewModel.setBeiShu(multiple_of_Mathematics.toInt())
        }

        //前数的对应
        digitalViewModel.theFirstNumber.observe(this, {
            val number = it * digitalViewModel.getBeiShu()
            binding.number1.text = "$number"
        })

        //后数的对应
        digitalViewModel.theLastNumber.observe(this, {
            val number = it * digitalViewModel.getBeiShu()
            binding.number2.text = "$number"
        })

        //倍数的对应
        digitalViewModel.beiShu.observe(this, {
            Toast.makeText(this, "当前数学的倍数是：$it", Toast.LENGTH_SHORT).show()
        })

        //各答案的对应
        digitalViewModel.theAnswer.observe(this, {
            var list = listOf<Int>(0, 0, 0)

            while (list[0] == list[1] || list[0] == list[2] || list[1] == list[2]
                || list[0] < 0 || list[1] < 0 || list[2] < 0
            ) {
                list = listOf<Int>(
                    it * digitalViewModel.getBeiShu(),
                    (it + Tools.randomNum(-2, 4)) * digitalViewModel.getBeiShu(),
                    (it + Tools.randomNum(-1, 1)) * digitalViewModel.getBeiShu()
                )
            }


            val startInt = Tools.randomNum(0, 2)

            binding.answer1.text = list[startInt % 3].toString()
            binding.answer2.text = list[(startInt + 1) % 3].toString()
            binding.answer3.text = list[(startInt + 2) % 3].toString()

        })

        //countdownTime = 10
        examCountdown = object : CountDownTimer(
            (digital_preferences_is_set_count_down_second * 1000).toLong(),
            digital_preferences_is_set_count_down_second.toLong()
        ) {
            //android:id="@+id/ll_test_time"
            override fun onTick(millisUntilFinished: Long) {

                //val second = ceil(millisUntilFinished / 1000.0).toInt()

                val persen =
                    millisUntilFinished / (1000.0 * digital_preferences_is_set_count_down_second)
                val width = (getScreenWidth() * persen).toInt()

                binding.llTestTime.width = width
            }

            override fun onFinish() {
                answerWrong("时间到了")
            }

        }


    }

    private fun insertScore(score: Int) {
        val mathScore = MathScore(0, TAG, score, Tools.getDate(), Tools.getDateTime())
        mMathScoreDbViewModel.insert(mathScore)
    }

    private fun getMaxScore(date: String, id: String): Int {
        return mMathScoreDbViewModel.getMaxScore(date, id)
    }

    override fun finish() {
        super.finish()
        examCountdown.cancel()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        examCountdown.cancel()
    }

    private fun checkAnswer(selectAnswer: Int): Boolean {
        val beiShu: Int = digitalViewModel.getBeiShu()
        if (selectAnswer == digitalViewModel.theAnswer.value!! * beiShu) {
            return true
        }
        return false
    }

    override fun onClick(v: View?) {
        examCountdown.cancel()
        when (v?.id) {
            R.id.nextTest -> {
                initNumber()
                mMathScoreDbViewModel.getMaxScoreLive(Tools.getDate(), TAG).observe(this, { value ->
                    value?.let {

                        binding.maxScore.text = "今天最高分：" + it.toString()
                    }
                })
            }
            R.id.ll_text -> {
                speakMsg(speakContent(), speakContent())
            }
            R.id.answer_1 -> {
                binding.answer1.setTextColor(Color.RED)
                answerAction(binding.answer1.text.toString())
            }
            R.id.answer_2 -> {
                binding.answer2.setTextColor(Color.RED)
                //binding.answer2.setTextColor(Color.RED)
                answerAction(binding.answer2.text.toString())
            }
            R.id.answer_3 -> {
                binding.answer3.setTextColor(Color.RED)
                answerAction(binding.answer3.text.toString())
            }
        }
    }

    private fun answerAction(answer: String) {
        val myView = TextView(this)
        myView.textSize = 50.0f
        myView.text = srcAnswer()

        speakMsg(speakContentAnswer(), speakContentAnswer())
        AlertDialog.Builder(this)
            .setCustomTitle(myView)
            .setPositiveButton("OK") { _, _ ->
//                if (checkAnswer(answer.toInt())) {
//                    answerRight(answer)
//                } else {
//                    answerWrong(answer)
//                }
            }
            .setOnDismissListener {
                if (checkAnswer(answer.toInt())) {
                    answerRight(answer)
                } else {
                    answerWrong(answer)
                }
            }
//            .setOnCancelListener {
//                if (checkAnswer(answer.toInt())) {
//                    answerRight(answer)
//                } else {
//                    answerWrong(answer)
//                }
//            }
            .show()
    }

    private fun answerWrong(answer: String) {
        toast.showCustomToast("$answer:❌", this)

        speakMsg("答错了。", "間違えます。")

        digitalViewModel.fenShuAdd(-digital_preferences_deduct_points_for_mistakes)
        if (digitalViewModel.getFenShu() < 0) {
            digitalViewModel.setFenShu(0)
        }


        //错了之后，是否马上下一题
        if (is_after_wrong_is_next) {
            binding.nextTest.visibility = View.VISIBLE
            binding.llAnswer.visibility = View.GONE
        } else {
            //如果打错了，不想要马上做下一题，再换选择新的答案列
            digitalViewModel.setAnswer(currentAnswer)
        }
    }


    private fun answerRight(answer: String) {
        toast.showCustomToast("$answer:◯", this)

        speakMsg("答对了。", "正解です。")

        digitalViewModel.fenShuAdd(1)
        binding.nextTest.visibility = View.VISIBLE
        binding.llAnswer.visibility = View.GONE
    }

    private fun initNumber() {
        toast.cancel()


        binding.answer1.setTextColor(Color.WHITE)
        binding.answer2.setTextColor(Color.WHITE)
        binding.answer3.setTextColor(Color.WHITE)

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
            var randomNum1 = getRandomNum1(randomMax)
            var randomNum2 = Tools.randomNum(0, randomMax)

            //最后的答案
            val lastAnswer = currentAnswer

            //选择答案设置
            currentAnswer = randomNum1 + randomNum2

            while (
                binding.number2.text == randomNum2.toString()
                || lastAnswer == currentAnswer
            ) {
                randomNum1 = getRandomNum1(randomMax)
                randomNum2 = Tools.randomNum(0, randomMax)

                currentAnswer = randomNum1 + randomNum2
            }
            digitalViewModel.setFirstNumber(
                randomNum1
            )
            digitalViewModel.setLastNumber(
                randomNum2
            )
            digitalViewModel.setAnswer(currentAnswer)


            binding.tvNum1.text = addViewText(randomNum1)
            binding.tvNum2.text = addViewText(randomNum2)


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
            var randomNum1 = getRandomNum1(randomMax)
            var randomNum2 = Tools.randomNum(0, randomMax)

            //最后的答案
            val lastAnswer = currentAnswer

            //选择答案设置
            currentAnswer = randomNum1 - randomNum2
            while (
                randomNum1 <= randomNum2
                || binding.number2.text == randomNum2.toString()
                || randomNum1 - randomNum2 < 0
                || lastAnswer == currentAnswer
            ) {
                randomNum1 = getRandomNum1(randomMax)
                randomNum2 = Tools.randomNum(0, randomMax)

                /* 为了少一点等于0 begin*/
                if (randomNum2 == 0) randomNum2 = Tools.randomNum(0, randomMax)
                if (randomNum2 == 0) randomNum2 = Tools.randomNum(0, randomMax)
                /* 为了少一点等于0 end*/

                currentAnswer = randomNum1 - randomNum2
            }

            digitalViewModel.setFirstNumber(
                randomNum1
            )
            digitalViewModel.setLastNumber(
                randomNum2
            )
            digitalViewModel.setAnswer(currentAnswer)


            binding.tvNum1.text = addViewText(randomNum1)
            binding.tvNum2.text = addViewText(randomNum2)

        }


        if (ThisApp.mAppViewModel.next_question_read.value == true) {
            speakMsg(speakContent(), speakContent())
        }

        binding.nextTest.visibility = View.GONE
        binding.llAnswer.visibility = View.VISIBLE


        examCountdown.cancel()
        if (is_set_count_down) {
            examCountdown.start()
        }
    }

    //获取第一个数
    private fun getRandomNum1(randomMax: Int) = if (is_set_first_num) {
        randomMax
    } else {
        Tools.randomNum(1, randomMax)
    }


    private fun addViewText(randomNum: Int): String {
        var r = ""
        for (i in 0 until randomNum) {
            r += "〇"
        }
        return r
    }

    private fun speakContent(): String {
        val number1Text: String = binding.number1.text.toString()
        val number2Text: String = binding.number2.text.toString()
        return if (is_speak_chinese) {
            "$number1Text $plus $number2Text 等于 多少？"
        } else {
            "$number1Text $plus $number2Text は 何ですか？"
        }
        return ""
    }

    //显示答案用的阅读文本
    private fun speakContentAnswer(): String {
        val number1Text: String = binding.number1.text.toString()
        val number2Text: String = binding.number2.text.toString()
        var match = "加"
        match = if (is_speak_chinese) {
            "加"
        } else {
            "たす"
        }
        var answer =
            binding.number1.text.toString().toInt() + binding.number2.text.toString().toInt()
        if (!is_add_match) {
            match = "减"
            match = if (is_speak_chinese) {
                "减"
            } else {
                "ひく"
            }
            answer =
                binding.number1.text.toString().toInt() - binding.number2.text.toString().toInt()
        }
        return "$number1Text $match $number2Text = $answer"
    }

    //显示答案用的文本
    private fun srcAnswer(): String {
        val number1Text: String = binding.number1.text.toString()
        val number2Text: String = binding.number2.text.toString()
        var answer =
            binding.number1.text.toString().toInt() + binding.number2.text.toString().toInt()
        var match = "+"
        if (!is_add_match) {
            match = "-"
            answer =
                binding.number1.text.toString().toInt() - binding.number2.text.toString().toInt()
        }
        return "$number1Text $match $number2Text = $answer"
    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        super.onInit(status)

        // 音声合成の実行
        speakMsg("开始", "始めます")
    }
}

