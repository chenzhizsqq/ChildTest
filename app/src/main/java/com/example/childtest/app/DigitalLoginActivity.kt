package com.example.childtest.app

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.childtest.R
import com.example.childtest.databinding.ActivityLoginDigitalBinding
import java.util.*


class DigitalLoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginDigitalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginDigitalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.digital_settings, DigitalSettingsFragment())
                .commit()
        }

        binding.next.setOnClickListener {

            val intent =
                Intent(this@DigitalLoginActivity, DigitalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    class DigitalSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.digital_preferences, rootKey)
        }
    }
}

