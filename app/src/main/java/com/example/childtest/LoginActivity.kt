package com.example.childtest

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.childtest.databinding.ActivityLoginBinding
import java.util.*

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
    private val bTimeAble: Boolean by lazy {
        ThisApp.sharedPreGetBoolean("time_limit")
    }

    //剩余解锁的时间 秒
    private val remainingTime_ss: Int by lazy {
        ThisApp.sharedPreGetInt(Config.remaining_time_ss)
    }

    //锁屏需要的时间
    private val timeSetting_mm: Int by lazy {
        ThisApp.sharedPreGetInt("lock_use_time")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mKeyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

        if (bWelcomePassword == true) {
            binding.root.setOnClickListener {
                checkCredential(this@LoginActivity)
            }
        } else {
            if (bTimeAble) {
                Log.d(TAG, "onCreate: remainingTime秒:$remainingTime_ss")
                if (remainingTime_ss > 0) {
                    LoginSuccess()
                } else {
                    binding.root.setOnClickListener {
                        checkCredential(this@LoginActivity)
                    }
                }
            } else {
                LoginSuccess()
            }
        }
    }


    private fun checkCredential(context: Context) {
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        //端末認証が有効か?
        if (keyguardManager?.isKeyguardSecure == true) {
            //OS10以上か?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val biometricPrompt =
                    //OS11以上か?
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        BiometricPrompt.Builder(context)
                            .setTitle("手机认证")
                            .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                            .build()
                    } else {
                        BiometricPrompt.Builder(context)
                            .setTitle("手机认证")
                            .setDeviceCredentialAllowed(true)
                            .build()
                    }

                //認証のキャンセル要求があった
                val cancellationSignal = CancellationSignal()
                cancellationSignal.setOnCancelListener {
                    //キャンセルされた
                    Toast.makeText(
                        applicationContext,
                        "認証 cancel: ", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                val executors = ContextCompat.getMainExecutor(context)
                val authCallBack = object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        //認証成功
                        LoginSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        //認証失敗 or ダイアログを消す
                        Toast.makeText(
                            applicationContext,
                            "認証失敗 $errString", Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                biometricPrompt.authenticate(cancellationSignal, executors, authCallBack)
            } else {

                showScreenLockPwd()

            }
        }
    }

    private fun LoginSuccess() {
        ThisApp.sharedPrePut(Config.remaining_time_ss, timeSetting_mm * 60)

        val intent =
            Intent(this@LoginActivity, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    //手机密码验证
    private fun showScreenLockPwd() {
        val intent: Intent = mKeyguardManager.createConfirmDeviceCredentialIntent(null, null)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: アプリ起動認証")
        Log.d(TAG, "onActivityResult: requestCode:$requestCode resultCode:$resultCode data:$data")
        if (requestCode == 2) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                //認証成功
                LoginSuccess()
            } else {
                //認証失敗
                Log.d(TAG, "onActivityResult: 認証失敗")
            }
        }
    }
}