package com.example.childtest.menu

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.childtest.R
import com.example.childtest.appConfig.Config
import com.example.childtest.appConfig.ThisApp
import com.example.childtest.databinding.SettingsActivityBinding
import com.example.childtest.db.MathScoreDbViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding

    val mathScoreDbViewModel: MathScoreDbViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        binding.cleanData.setOnClickListener {
            mathScoreDbViewModel.deleteAll()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()


        if (ThisApp.sharedPreferences.getBoolean("time_limit", false)) {
            ThisApp.sharedPrePut(
                Config.remaining_time_ss,
                ThisApp.sharedPreferences.getInt("lock_use_time", 0) * 60
            )
        }

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}