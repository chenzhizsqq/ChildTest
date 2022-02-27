package com.example.childtest.app

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.childtest.R
import com.example.childtest.databinding.ActivityJapaneseTestLoginBinding
import java.util.*


class JapaneseTestLoginActivity : BaseActivity() {

    private lateinit var binding: ActivityJapaneseTestLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJapaneseTestLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.digital_settings, SettingsFragment())
                .commit()
        }

        binding.next.setOnClickListener {

            val intent =
                Intent(this@JapaneseTestLoginActivity, JapaneseTestActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.japanese_test_login_preferences, rootKey)
        }
    }
}

