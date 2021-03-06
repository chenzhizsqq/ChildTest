package com.example.childtest.app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.childtest.*
import com.example.childtest.appConfig.Config
import com.example.childtest.appConfig.ThisApp
import com.example.childtest.menu.LoginActivity
import com.example.childtest.menu.SettingLogoDialog
import com.example.childtest.menu.SettingsActivity
import java.util.*

open class BaseActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener    //共享属性的，属性变更的监听
    , TextToSpeech.OnInitListener                           //系统文字阅读的工具
{
    private val TAG = "BaseActivity"

    lateinit var tts: TextToSpeech

    //倒数器
    private lateinit var countDownTimer: CountDownTimer

    //是否有，时间限制
    private val bTimeAble: Boolean by lazy {
        ThisApp.sharedPreferences.getBoolean("time_limit", false)
    }

    /**
     * 读取内容 中文
     */
    fun speakMsgCn(chinese: String) {
        //是否中文
        val is_speak_chinese =
            ThisApp.sharedPreferences.getBoolean("digital_preferences_speak_chinese", true)

        if (!is_speak_chinese) {
            Toast.makeText(this, "暂时是日语读取", Toast.LENGTH_SHORT).show()
            return
        }

        this.tts.speak(chinese, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }

    /**
     * 读取内容 日文
     */
    fun speakMsgJp(japanese: String) {
        //是否中文
        val is_speak_chinese =
            ThisApp.sharedPreferences.getBoolean("digital_preferences_speak_chinese", true)

        if (is_speak_chinese) {
            Toast.makeText(this, "暂时是中文读取", Toast.LENGTH_SHORT).show()
            return
        }

        this.tts.speak(japanese, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }

    /**
     * 读取内容 中文 和 日文
     * chinese 中文
     * japanese 日文
     */
    fun speakMsg(chinese: String, japanese: String) {
        //是否中文
        val is_speak_chinese =
            ThisApp.sharedPreferences.getBoolean("digital_preferences_speak_chinese", true)

        val speakMsg = if (is_speak_chinese) {
            chinese
        } else {
            japanese
        }

        this.tts.speak(speakMsg, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
    }


    //https://www.geeksforgeeks.org/how-to-add-a-custom-styled-toast-in-android-using-kotlin/
    //创建自定义的提示框
    fun Toast.showCustomToast(message: String, activity: Activity) {
        val layout = activity.layoutInflater.inflate(
            R.layout.custom_toast_layout,
            activity.findViewById(R.id.toast_container)
        )

        // set the text of the TextView of the message
        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = message

        // use the application extension function
        this.apply {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)

        ThisApp.mAppViewModel.next_question_read.value =
            ThisApp.sharedPreferences.getBoolean("next_question_read", false)
        ThisApp.mAppViewModel.random_select.value =
            ThisApp.sharedPreferences.getBoolean("random_select", false)

        //tips_is_show
        ThisApp.mAppViewModel.tips_is_show.value =
            ThisApp.sharedPreferences.getBoolean("tips_is_show", false)

        //Log.e(TAG_BaseActivity, "onCreate: ")
        if (bTimeAble) {

            val timeSetting_ss = ThisApp.sharedPreferences.getInt("lock_use_time", 30) * 60
            //Log.e(TAG_BaseActivity, "onCreate: timeSetting_ss:$timeSetting_ss")

            val remainingTime_ss =
                ThisApp.sharedPreferences.getInt(Config.remaining_time_ss, 30 * 60)
            //Log.e(TAG_BaseActivity, "onCreate: remainingTime_ss:$remainingTime_ss")

            if (timeSetting_ss < remainingTime_ss) {
                countDownTimerMM(timeSetting_ss.toLong())
            } else {
                countDownTimerMM(remainingTime_ss.toLong())
            }
        }

        //共享属性，追加属性变化的监听
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    //引用右上角的功能选择器 - 创建
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        menu.findItem(R.id.next_question_read).isChecked =
            ThisApp.sharedPreferences.getBoolean("next_question_read", false)
        menu.findItem(R.id.random_select).isChecked =
            ThisApp.sharedPreferences.getBoolean("random_select", false)
        menu.findItem(R.id.tips_is_show).isChecked =
            ThisApp.sharedPreferences.getBoolean("tips_is_show", false)
        return true
    }

    //右上角选择器，选择后的处理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val mSettingLogoDialog = SettingLogoDialog()
                mSettingLogoDialog.apply {
                    show(supportFragmentManager, "SettingLogoDialog")
                }
                mSettingLogoDialog.setOnDialogListener(object : SettingLogoDialog.OnDialogListener {
                    override fun onClick(bOpen: Boolean) {
                        if (bOpen) {
                            val intent =
                                Intent(this@BaseActivity, SettingsActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "错了，请再输入一次", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
            }
            R.id.next_question_read -> {
                item.isChecked = !item.isChecked
                ThisApp.sharedPrePut("next_question_read", item.isChecked)
                ThisApp.mAppViewModel.next_question_read.value = item.isChecked
                return true
            }
            R.id.random_select -> {
                item.isChecked = !item.isChecked
                ThisApp.sharedPrePut("random_select", item.isChecked)
                ThisApp.mAppViewModel.random_select.value = item.isChecked
                return true
            }
            R.id.tips_is_show -> {
                item.isChecked = !item.isChecked
                ThisApp.sharedPrePut("tips_is_show", item.isChecked)
                ThisApp.mAppViewModel.tips_is_show.value = item.isChecked
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //最后剩余时间 秒
    private var lastRemainingTime_ss: Int = 0

    //倒数时间的设定
    fun countDownTimerMM(ss: Long) {
        countDownTimer = object : CountDownTimer(ss * 1000, 300) {

            //倒数完的处理
            override fun onFinish() {
                if (bTimeAble) {
                    Log.e(TAG, "countDownTimerSS onFinish(): ")

                    ThisApp.sharedPrePut(Config.remaining_time_ss, 0)

                    //倒数时间完了，退到LoginActivity页上。
                    val intent =
                        Intent(this@BaseActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            //倒数中的状况
            override fun onTick(millisUntilFinished: Long) {
                if (bTimeAble) {

                    val hour = millisUntilFinished / 1000 / 60 / 60
                    val minute = millisUntilFinished / 1000 / 60 % 60
                    val second = millisUntilFinished / 1000 % 60
                    //Log.e(TAG_BaseActivity, "onTick: 倒计时"+hour+"小时"+minute+"分"+second+"秒", )
                    lastRemainingTime_ss = (millisUntilFinished / 1000).toInt()

                }

            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (bTimeAble) {
            countDownTimer.cancel()
            ThisApp.sharedPrePut(Config.remaining_time_ss, lastRemainingTime_ss)
            Log.d(
                TAG,
                "onDestroy: remaining_time" + ThisApp.sharedPreferences.getInt(
                    Config.remaining_time_ss,
                    0
                )
            )
        }

        //共享属性，去除属性变化的监听
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    //返回到主目录上
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    //共享属性，监听时的监听数据变化函数
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        /*when (key) {
            "next_question_read" -> {
                if (sharedPreferences != null) {
                    bNextQuestionRead = sharedPreferences.getBoolean("next_question_read", false)
                }
            }
            "bRandomSelect" -> {
                if (sharedPreferences != null) {
                    bRandomSelect = sharedPreferences.getBoolean("random_select", false)
                }
            }

        }*/
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status [TextToSpeech.SUCCESS] or [TextToSpeech.ERROR].
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            //是否中文
            val is_speak_chinese =
                ThisApp.sharedPreferences.getBoolean("digital_preferences_speak_chinese", true)

            // ロケールの指定
            if (is_speak_chinese) {

                val locale = Locale.CHINA
                if (this.tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {

                    val chineseSpeak = ThisApp.sharedPreferences.getString("chineseSpeak", "")
                    tts.language = Locale("zh", chineseSpeak)
                }
            } else {

                val locale = Locale.JAPAN
                if (this.tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                    tts.language = Locale.JAPAN
                }

            }

            // 音声合成の実行
            this.tts.speak(" ", TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }
    }


    fun getScreenWidth(): Int {
        var ScreenWidth = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = this.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

            ScreenWidth = windowMetrics.bounds.width()
            val StatusBar: Int = insets.top
            val NavigationBar: Int = insets.bottom
        } else {
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            val disp = wm.defaultDisplay

            val realSize = Point()
            disp.getRealSize(realSize)

            ScreenWidth = realSize.x
        }

        return ScreenWidth
    }

    fun getScreenHeight(): Int {
        var ScreenHeight = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = this.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

            ScreenHeight = windowMetrics.bounds.height()
            val StatusBar: Int = insets.top
            val NavigationBar: Int = insets.bottom
        } else {
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            val disp = wm.defaultDisplay

            val realSize = Point()
            disp.getRealSize(realSize)
            ScreenHeight = realSize.y
        }

        return ScreenHeight
    }
}