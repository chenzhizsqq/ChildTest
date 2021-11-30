package com.example.childtest

import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityTextToSpeechBinding
import java.util.*

class TextToSpeechActivity : BaseActivity(), TextToSpeech.OnInitListener {
    private val TAG = "TextToSpeechActivity"

    private var tts: TextToSpeech? = null

    private lateinit var binding: ActivityTextToSpeechBinding

    private var randomInt = 0

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }
    private val bClickedRead: Boolean? by lazy {
        sharedPreferences.getBoolean("clicked_read", false)
    }
    private val bRandomSelect: Boolean? by lazy {
        sharedPreferences.getBoolean("random_select", true)
    }

    private var intMax = 100

    // 起動時に呼ばれる
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextToSpeechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.speech_settings, SpeechFragment())
                .commit()
        }

        intMax = sharedPreferences.getString("speech_preferences_max_num", "100")?.toInt() ?: 100

        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key){
                "speech_preferences_max_num" -> {
                    intMax = sharedPreferences.getString(key, "100")?.toInt() ?: 100
                }
            }
        }

        // TextToSpeechの生成
        this.tts = TextToSpeech(this, this)

        binding.number.text = randomInt.toString()

        binding.number.setOnClickListener { // 执行朗读

            if (bRandomSelect == true) {
                randomInt = Tools.randomNum(0, intMax)
            } else {
                randomInt++
                if (randomInt > intMax) {
                    randomInt = 0
                }
            }

            binding.number.text = randomInt.toString()

            if (bClickedRead == true) {
                this.tts!!.speak(
                    randomInt.toString(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "utteranceId"
                )
            }
        }

        binding.numberSpeak.setOnClickListener {
            this.tts!!.speak(randomInt.toString(), TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }
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
            }

            binding.number.text = randomInt.toString()

            if (bClickedRead == true) {
                // 音声合成の実行
                this.tts!!.speak(
                    randomInt.toString(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "utteranceId"
                )
            }
        }
    }

    class SpeechFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.speech_preferences, rootKey)
        }
    }
}