package com.example.childtest.test

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.childtest.databinding.AdapterTestPostsBinding

class TestPostsAdapter :
    RecyclerView.Adapter<TestPostsAdapter.ViewHolder>() {
    private val list: ArrayList<PostsData> by lazy {
        ArrayList()
    }

    inner class ViewHolder(binding: AdapterTestPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_id: TextView = binding.id
        val tv_title: TextView = binding.title
    }

    //添加数据
    fun notifyDataAddData(list: ArrayList<PostsData>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterTestPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_id.text = list[position].id.toString()
        holder.tv_title.text = list[position].title
    }
}