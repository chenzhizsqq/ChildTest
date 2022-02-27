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
import com.example.childtest.databinding.ActivityJapaneseTestBinding


class JapaneseTestActivity : BaseActivity(), TextToSpeech.OnInitListener, View.OnClickListener {

    private lateinit var binding: ActivityJapaneseTestBinding

    private val TAG = "JapaneseTestActivity"
    private var plus = "加"
    private var currentAnswer = 0

    //错了之后，是否马上下一题
    val is_after_wrong_is_next =
        ThisApp.sharedPreferences.getBoolean("after_wrong_is_next", true)

    private val viewModel: JapaneseTestViewModel by lazy {
        ViewModelProvider(this)[JapaneseTestViewModel::class.java]
    }

    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJapaneseTestBinding.inflate(layoutInflater)
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

        toast = Toast(this)


        //让分数添加监视
        viewModel.fenShu.observe(this, {
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

    private fun checkAnswer(selectAnswer: String): Boolean {
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nextTest -> {
                initNumber()
            }
            R.id.ll_text -> {
                speakMsgJp( "speakContent")
            }
            R.id.answer_1 -> {
                if (checkAnswer(binding.answer1.text.toString())) {
                    answerRight(binding.answer1.text.toString())
                } else {
                    answerWrong(binding.answer1.text.toString())
                }
            }
            R.id.answer_2 -> {

                if (checkAnswer(binding.answer2.text.toString())) {
                    answerRight(binding.answer2.text.toString())
                } else {
                    answerWrong(binding.answer2.text.toString())
                }
            }
            R.id.answer_3 -> {

                if (checkAnswer(binding.answer3.text.toString())) {
                    answerRight(binding.answer3.text.toString())
                } else {
                    answerWrong(binding.answer3.text.toString())
                }
            }
        }
    }

    private fun answerWrong(answer: String) {
        toast.showCustomToast("$answer:❌", this)

        speakMsgJp( "間違えます。")

        viewModel.fenShu.value?.plus(-1)


        //错了之后，是否马上下一题
        if (is_after_wrong_is_next) {
            binding.nextTest.visibility = View.VISIBLE
            binding.llAnswer.visibility = View.GONE
        }
    }


    private fun answerRight(answer: String) {
        toast.showCustomToast("$answer:◯", this)

        speakMsgJp( "正解です。")

        viewModel.fenShu.value?.plus(1)
        binding.nextTest.visibility = View.VISIBLE
        binding.llAnswer.visibility = View.GONE
    }

    private fun initNumber() {
        toast.cancel()

        binding.llText.visibility = View.VISIBLE
        binding.llAnswer.visibility = View.VISIBLE


        if (ThisApp.mAppViewModel.next_question_read.value == true) {
            speakMsgJp( "speakContent")
        }

        binding.nextTest.visibility = View.GONE
        binding.llAnswer.visibility = View.VISIBLE
    }



    private fun addViewText(randomNum: Int): String {
        var r = ""
        for (i in 0 until randomNum) {
            r += "〇"
        }
        return r
    }

    // TextToSpeechの初期化完了時に呼ばれる
    override fun onInit(status: Int) {
        super.onInit(status)

        // 音声合成の実行
        speakMsgJp( "始めます")
    }
}

