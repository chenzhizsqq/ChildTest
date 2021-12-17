package com.example.childtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.childtest.databinding.ActivityTestBinding
import com.example.childtest.databinding.ActivityTextToSpeechBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class TestActivity : BaseActivity() {
    private val TAG = "TestActivity"
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jsonTestButton.setOnClickListener {
            githubJsonServiceGetMethod()
        }
    }


    fun githubJsonServiceGetMethod() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .build()

        val service = retrofit.create(GithubJsonService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getTest()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    binding.jsonCodeTextview.text = response.code().toString()
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val json = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string()
                        )
                    )
                    binding.jsonResultsTextview.text = json

                    val jsonData : GithubJson_test = gson.fromJson(json,GithubJson_test::class.java)
                    Log.e(TAG, "getMethodTest: jsonData.id: "+jsonData.id )
                    Log.e(TAG, "getMethodTest: jsonData.title: "+jsonData.title )

                }
            }
        }
    }
}