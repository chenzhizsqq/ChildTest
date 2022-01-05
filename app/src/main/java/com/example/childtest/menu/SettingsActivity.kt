package com.example.childtest.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.childtest.R
import com.example.childtest.appConfig.Config
import com.example.childtest.appConfig.ThisApp

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()


        if (ThisApp.sharedPreferences.getBoolean("time_limit",false)) {
            ThisApp.sharedPrePut(
                Config.remaining_time_ss,
                ThisApp.sharedPreferences.getInt("lock_use_time",0) * 60
            )
        }

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}