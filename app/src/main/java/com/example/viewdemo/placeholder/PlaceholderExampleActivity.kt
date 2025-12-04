package com.example.viewdemo.placeholder

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.hm.viewdemo.R

/**
 * PlaceholderView 使用示例
 * 演示如何使用自定义占位符View来显示加载状态
 */
class PlaceholderExampleActivity : AppCompatActivity() {

    private lateinit var hotListCard: CardView
    private lateinit var topicDetailCard: CardView
    private lateinit var commentCard: CardView
    private lateinit var fixedHeightCard: CardView
    
    private lateinit var hotListPlaceholder: PlaceholderView
    private lateinit var topicDetailPlaceholder: PlaceholderView
    private lateinit var commentPlaceholder: PlaceholderView
    private lateinit var fixedHeightPlaceholder: PlaceholderView2
    
    private lateinit var hotListContent: LinearLayout
    private lateinit var topicDetailContent: LinearLayout
    private lateinit var commentContent: LinearLayout
    private lateinit var fixedHeightContent: LinearLayout
    
    private lateinit var btnToggleHotList: Button
    private lateinit var btnToggleTopicDetail: Button
    private lateinit var btnToggleComment: Button
    private lateinit var btnToggleFixedHeight: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeholder_example)

        initViews()
        setupClickListeners()
        
        // 初始状态显示占位符
        showPlaceholders()
    }

    private fun initViews() {
        // 卡片容器
        hotListCard = findViewById(R.id.hotListCard)
        topicDetailCard = findViewById(R.id.topicDetailCard)
        commentCard = findViewById(R.id.commentCard)
        fixedHeightCard = findViewById(R.id.fixedHeightCard)
        
        // 占位符视图
        hotListPlaceholder = findViewById(R.id.hotListPlaceholder)
        topicDetailPlaceholder = findViewById(R.id.topicDetailPlaceholder)
        commentPlaceholder = findViewById(R.id.commentPlaceholder)
        fixedHeightPlaceholder = findViewById(R.id.fixedHeightPlaceholder)
        
        // 实际内容视图
        hotListContent = findViewById(R.id.hotListContent)
        topicDetailContent = findViewById(R.id.topicDetailContent)
        commentContent = findViewById(R.id.commentContent)
        fixedHeightContent = findViewById(R.id.fixedHeightContent)
        
        // 按钮
        btnToggleHotList = findViewById(R.id.btnToggleHotList)
        btnToggleTopicDetail = findViewById(R.id.btnToggleTopicDetail)
        btnToggleComment = findViewById(R.id.btnToggleComment)
        btnToggleFixedHeight = findViewById(R.id.btnToggleFixedHeight)
        
        // 自定义占位符样式
        setupPlaceholderStyles()
    }

    private fun setupPlaceholderStyles() {
        // 热议榜占位符 - 使用默认样式
        
        // 话题详情占位符 - 稍微不同的样式
        topicDetailPlaceholder.apply {
            setBarHeight(10f)
            setBarSpacing(6f)
            setPlaceholderColor(Color.parseColor("#F0F0F0"))
        }
        
        // 评论占位符 - 更小的条
        commentPlaceholder.apply {
            setBarHeight(8f)
            setBarSpacing(4f)
            setCornerRadius(4f)
            setPlaceholderColor(Color.parseColor("#EEEEEE"))
        }
    }

    private fun setupClickListeners() {
        btnToggleHotList.setOnClickListener {
            toggleHotListState()
        }
        
        btnToggleTopicDetail.setOnClickListener {
            toggleTopicDetailState()
        }
        
        btnToggleComment.setOnClickListener {
            toggleCommentState()
        }
        
        btnToggleFixedHeight.setOnClickListener {
            toggleFixedHeightState()
        }
    }

    private fun showPlaceholders() {
        // 显示所有占位符，隐藏实际内容
        hotListPlaceholder.visibility = android.view.View.VISIBLE
        hotListContent.visibility = android.view.View.GONE
        
        topicDetailPlaceholder.visibility = android.view.View.VISIBLE
        topicDetailContent.visibility = android.view.View.GONE
        
        commentPlaceholder.visibility = android.view.View.VISIBLE
        commentContent.visibility = android.view.View.GONE
        
        fixedHeightPlaceholder.visibility = android.view.View.VISIBLE
        fixedHeightContent.visibility = android.view.View.GONE
        
        // 更新按钮文字
        btnToggleHotList.text = "显示热议榜内容"
        btnToggleTopicDetail.text = "显示话题详情内容"
        btnToggleComment.text = "显示评论内容"
        btnToggleFixedHeight.text = "显示固定高度内容"
    }

    private fun toggleHotListState() {
        if (hotListPlaceholder.visibility == android.view.View.VISIBLE) {
            // 显示加载中，然后显示内容
            btnToggleHotList.text = "加载中..."
            btnToggleHotList.isEnabled = false
            
            Handler(Looper.getMainLooper()).postDelayed({
                hotListPlaceholder.visibility = android.view.View.GONE
                hotListContent.visibility = android.view.View.VISIBLE
                btnToggleHotList.text = "显示占位符"
                btnToggleHotList.isEnabled = true
            }, 1500)
        } else {
            hotListPlaceholder.visibility = android.view.View.VISIBLE
            hotListContent.visibility = android.view.View.GONE
            btnToggleHotList.text = "显示热议榜内容"
        }
    }

    private fun toggleTopicDetailState() {
        if (topicDetailPlaceholder.visibility == android.view.View.VISIBLE) {
            btnToggleTopicDetail.text = "加载中..."
            btnToggleTopicDetail.isEnabled = false
            
            Handler(Looper.getMainLooper()).postDelayed({
                topicDetailPlaceholder.visibility = android.view.View.GONE
                topicDetailContent.visibility = android.view.View.VISIBLE
                btnToggleTopicDetail.text = "显示占位符"
                btnToggleTopicDetail.isEnabled = true
            }, 1500)
        } else {
            topicDetailPlaceholder.visibility = android.view.View.VISIBLE
            topicDetailContent.visibility = android.view.View.GONE
            btnToggleTopicDetail.text = "显示话题详情内容"
        }
    }

    private fun toggleCommentState() {
        if (commentPlaceholder.visibility == android.view.View.VISIBLE) {
            btnToggleComment.text = "加载中..."
            btnToggleComment.isEnabled = false
            
            Handler(Looper.getMainLooper()).postDelayed({
                commentPlaceholder.visibility = android.view.View.GONE
                commentContent.visibility = android.view.View.VISIBLE
                btnToggleComment.text = "显示占位符"
                btnToggleComment.isEnabled = true
            }, 1500)
        } else {
            commentPlaceholder.visibility = android.view.View.VISIBLE
            commentContent.visibility = android.view.View.GONE
            btnToggleComment.text = "显示评论内容"
        }
    }

    private fun toggleFixedHeightState() {
        if (fixedHeightPlaceholder.visibility == android.view.View.VISIBLE) {
            btnToggleFixedHeight.text = "加载中..."
            btnToggleFixedHeight.isEnabled = false
            
            Handler(Looper.getMainLooper()).postDelayed({
                fixedHeightPlaceholder.visibility = android.view.View.GONE
                fixedHeightContent.visibility = android.view.View.VISIBLE
                btnToggleFixedHeight.text = "显示占位符"
                btnToggleFixedHeight.isEnabled = true
            }, 1500)
        } else {
            fixedHeightPlaceholder.visibility = android.view.View.VISIBLE
            fixedHeightContent.visibility = android.view.View.GONE
            btnToggleFixedHeight.text = "显示固定高度内容"
        }
    }
}