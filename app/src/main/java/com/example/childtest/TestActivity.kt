package com.example.childtest

import android.os.Bundle
import android.util.Log
import com.example.childtest.databinding.ActivityTestBinding
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
            jsonGet()
        }
    }


    private fun jsonGet() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .build()

        val service = retrofit.create(GithubJsonService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getResponse()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    binding.jsonCodeTextview.text = response.code().toString()
                    val gson = GsonBuilder().setPrettyPrinting().create()

                    //jsonOrg获取原来的json的样式
                    val jsonOrg = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string()
                        )
                    )

                    binding.jsonResultsTextview.text = jsonOrg

                    //把Json通过gson，转成结构的数据
                    val jsonData : JsonDataClass = gson.fromJson(jsonOrg,JsonDataClass::class.java)
                    Log.e(TAG, "getMethodTest: jsonData.id: "+jsonData.id )
                    Log.e(TAG, "getMethodTest: jsonData.title: "+jsonData.title )

                }
            }
        }
    }
}