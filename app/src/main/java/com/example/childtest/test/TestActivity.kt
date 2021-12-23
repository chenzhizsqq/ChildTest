package com.example.childtest.test

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.childtest.app.BaseActivity
import com.example.childtest.databinding.ActivityTestBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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

    private val viewModel = TestViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jsonTestButton.setOnClickListener {
            jsonGet()
        }

        binding.jsonGetPostsButton.setOnClickListener {
            testPostsAdapter.notifyDatClearData()
            jsonGetPosts()
        }

        //绑定recyclerview的adapter
        binding.recyclerview.adapter = testPostsAdapter

        //observe观察。这里意思就是movieLiveData被观察中，一旦postsLiveData接收数据，就会做出相对应的操作
        viewModel.postsDataList.observe(this, {
            testPostsAdapter.notifyDataAddData(it as ArrayList<PostsData>)
        })

        //观察是否正在读取数据，做出不同的操作
        viewModel.isLoading.observe(this, {
            if (viewModel.isLoading.value == true) {
                binding.isLoading.visibility = View.VISIBLE
            } else {
                binding.isLoading.visibility = View.GONE
            }
        })

        //添加recyclerview行数的管理器
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = linearLayoutManager

        //右下的一个浮动添加图标
        val fab: ExtendedFloatingActionButton = binding.extendedFAB

        //添加滚动监听，获取更多数据
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //滚动完时
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //检测当前数据，做出对应的操作
                if(viewModel.postsDataList.value?.isNotEmpty() == true){
                    val totalItemCount = recyclerView.layoutManager?.itemCount
                    val lastVisibleItemPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
                    //向上滚动，滚到最后一个后，添加发信
                    if (!viewModel.isLoading.value!! && totalItemCount == lastVisibleItemPosition + 1) {
                        jsonGetPosts()
                    }
                }else{
                    jsonGetPosts()
                }
            }

            //滚动时候
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 收缩
                if (dy > 10 && fab.isExtended) {
                    fab.shrink()
                }

                // 如果在上面，就扩张
                if (dy < -10 && !fab.isExtended) {
                    fab.extend()
                }

                // 如果时第一个，扩张
                if (!recyclerView.canScrollVertically(-1)) {
                    fab.extend()
                }
            }
        })

        //向下拉动，添加拉动监听，重新获取数据
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            testPostsAdapter.notifyDatClearData()
            jsonGetPosts()
        }
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

        viewModel.isLoading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = service.getResponsePosts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    //因为上面用上了.addConverterFactory，才可以直接联系LiveData.postValue。发送数据给postsLiveData
                    viewModel.postsDataList.postValue(response.body())

                    viewModel.isLoading.value = false
                } else {
                    Log.e(TAG, "jsonGetPosts: error:" + response.message())
                }
            }
        }
    }
}