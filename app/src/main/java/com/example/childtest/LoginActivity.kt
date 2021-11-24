package com.example.childtest

import android.app.KeyguardManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mKeyguardManager: KeyguardManager

    private val sharedPreferences: SharedPreferences? by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }
    private val bWelcomePassword: Boolean? by lazy {
        sharedPreferences?.getBoolean("welcome_password", true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (bWelcomePassword == true) {
            mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            showScreenLockPwd()

            binding.root.setOnClickListener {
                showScreenLockPwd()
            }
        } else {
            val intent =
                Intent(this@LoginActivity, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivityResult: アプリ起動認証")
        Log.e(TAG, "onActivityResult: requestCode:$requestCode resultCode:$resultCode data:$data")
        if (requestCode == 2) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                //認証成功
                val intent =
                    Intent(this@LoginActivity, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                //認証失敗
                Log.e(TAG, "onActivityResult: 認証失敗")
            }
        }
    }

    private fun showScreenLockPwd() {
        val intent: Intent = mKeyguardManager.createConfirmDeviceCredentialIntent(null, null)
        startActivityForResult(intent, 2)
    }
}