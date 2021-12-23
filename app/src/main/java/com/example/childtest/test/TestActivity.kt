package com.example.childtest.test

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.childtest.app.BaseActivity
import com.example.childtest.databinding.ActivityTestBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TestActivity : BaseActivity() {
    private val TAG = "TestActivity"
    private lateinit var binding: ActivityTestBinding

    //Adapter被绑定的对象，用作被recyclerview的绑定
    private val testPostsAdapter = TestPostsAdapter()

    //一个可以被改变数据的LiveData
    val postsLiveData = MutableLiveData<List<PostsData>>()

    //观察是否正在读取数据
    var isLoadingLiveData = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jsonTestButton.setOnClickListener {
            jsonGet()
        }

        binding.jsonGetPostsButton.setOnClickListener {
            jsonGetPosts()
        }

        //绑定recyclerview的adapter
        binding.recyclerview.adapter = testPostsAdapter

        //observe观察。这里意思就是movieLiveData被观察中，一旦postsLiveData接收数据，就会做出相对应的操作
        postsLiveData.observe(this, {
            testPostsAdapter.notifyDataAddData(it as ArrayList<PostsData>)
        })

        //观察是否正在读取数据，做出不同的操作
        isLoadingLiveData.observe(this, {
            if (isLoadingLiveData.value == true) {
                binding.isLoading.visibility = View.VISIBLE
            } else {
                binding.isLoading.visibility = View.GONE
            }
        })

        //添加recyclerview行数的管理器
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = linearLayoutManager

        //向上滚动，添加滚动监听，获取更多数据
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                val lastVisibleItemPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoadingLiveData.value!! && totalItemCount == lastVisibleItemPosition + 1) {
                    jsonGetPosts()
                }
            }
        })

    }

    //有意外发生时的对应线程
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "throwable: $throwable")
    }

    private fun jsonGet() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .build()

        val service = retrofit.create(GithubJsonService::class.java)

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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
                    val jsonData: TestData = gson.fromJson(jsonOrg, TestData::class.java)
                    Log.e(TAG, "getMethodTest: jsonData.id: " + jsonData.id)
                    Log.e(TAG, "getMethodTest: jsonData.title: " + jsonData.title)

                }
            }
        }
    }


    //https://github.com/chenzhizsqq/testJson/blob/main/db.json，"posts"那组数
    private fun jsonGetPosts() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create()) //把json转为gson，才可以直接用LiveData.postValue
            .build()

        val service = retrofit.create(GithubJsonService::class.java)

        isLoadingLiveData.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = service.getResponsePosts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    //因为上面用上了.addConverterFactory，才可以直接联系LiveData.postValue。发送数据给postsLiveData
                    postsLiveData.postValue(response.body())

                    isLoadingLiveData.value = false
                } else {
                    Log.e(TAG, "jsonGetPosts: error:" + response.message())
                }
            }
        }
    }
}