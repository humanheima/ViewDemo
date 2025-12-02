package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.DiscussionAdapter
import com.hm.viewdemo.adapter.HotTopicAdapter
import com.hm.viewdemo.model.Discussion
import com.hm.viewdemo.model.HotTopic

class HotTopicActivity : AppCompatActivity() {
    
    private lateinit var rvHotTopics: RecyclerView
    private lateinit var rvDiscussions: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_topic)
        
        initViews()
        setupRecyclerViews()
        loadData()
    }
    
    private fun initViews() {
        rvHotTopics = findViewById(R.id.rv_hot_topics)
        rvDiscussions = findViewById(R.id.rv_discussions)
    }
    
    private fun setupRecyclerViews() {
        // 设置热点榜RecyclerView
        rvHotTopics.layoutManager = LinearLayoutManager(this)
        
        // 设置讨论区RecyclerView
        rvDiscussions.layoutManager = LinearLayoutManager(this)
    }
    
    private fun loadData() {
        // 创建热点话题数据
        val hotTopics = listOf(
            HotTopic(1, 1, "#N大校园戏剧引发轰动#", "865120", true),
            HotTopic(2, 2, "#郁青宇新剧#", "865120", true),
            HotTopic(3, 3, "#N大戏剧社#", "865120", true),
            HotTopic(4, 4, "#校园艺术文化节#", "865120", true),
            HotTopic(5, 5, "#最近超火的美食#", "865120", true)
        )
        
        // 创建讨论数据
        val discussions = listOf(
            Discussion(
                1, "用户", R.drawable.default_avatar, "13分钟前",
                "为什么天天刷牙还有牙结石？", 8, 12
            ),
            Discussion(
                2, "用户", R.drawable.default_avatar, "13分钟前",
                "为什么天天刷牙还有牙结石？", 8, 12
            ),
            Discussion(
                3, "用户", R.drawable.default_avatar, "13分钟前",
                "为什么天天刷牙还有牙结石？", 8, 12
            )
        )
        
        // 设置适配器
        rvHotTopics.adapter = HotTopicAdapter(hotTopics)
        rvDiscussions.adapter = DiscussionAdapter(discussions)
    }
    
    companion object {
        fun launch(activity: AppCompatActivity) {
            val intent = Intent(activity, HotTopicActivity::class.java)
            activity.startActivity(intent)
        }
    }
}