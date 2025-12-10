package com.hm.viewdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.CustomPagerIndicator

/**
 * PagerAdapter 使用示例，配合自定义页面指示器
 * 展示如何实现跟随滑动的指示器效果
 */
class PagerAdapterExampleActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var indicator: CustomPagerIndicator
    
    // 示例页面数据
    private val pageData = listOf(
        PageItem("第一页", "#4B4854", "这是第一页的内容\n展示红色背景"),
        PageItem("第二页", "#4B4854", "这是第二页的内容\n展示蓝色背景"),
        PageItem("第三页", "#4B4854", "这是第三页的内容\n展示绿色背景"),
        PageItem("第四页", "#4B4854", "这是第四页的内容\n展示橙色背景"),
        PageItem("第五页", "#4B4854", "这是第五页的内容\n展示紫色背景")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager_adapter_example)
        
        initViews()
        setupViewPager()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.indicator)
    }

    private fun setupViewPager() {
        val adapter = CustomPagerAdapter()
        viewPager.adapter = adapter
        
        // 将指示器绑定到ViewPager
        indicator.attachToViewPager(viewPager)
    }

    /**
     * 自定义PagerAdapter
     */
    private inner class CustomPagerAdapter : PagerAdapter() {

        override fun getCount(): Int = pageData.size

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val pageItem = pageData[position]
            
            // 动态创建页面View
            val pageView = layoutInflater.inflate(R.layout.item_pager_page, container, false)
            
            // 设置页面内容
            val titleView = pageView.findViewById<android.widget.TextView>(R.id.tvTitle)
            val contentView = pageView.findViewById<android.widget.TextView>(R.id.tvContent)
            val backgroundView = pageView.findViewById<View>(R.id.viewBackground)
            
            titleView.text = pageItem.title
            contentView.text = pageItem.content
            backgroundView.setBackgroundColor(Color.parseColor(pageItem.backgroundColor))
            
            container.addView(pageView)
            return pageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pageData[position].title
        }
    }

    /**
     * 页面数据类
     */
    data class PageItem(
        val title: String,
        val backgroundColor: String,
        val content: String
    )
}