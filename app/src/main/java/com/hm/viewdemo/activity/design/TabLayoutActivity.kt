package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_tab_layout.*


/**
 * Created by dumingwei on 2020/4/28
 *
 * Desc: TabLayout使用
 */
class TabLayoutActivity : AppCompatActivity() {

    private val fragments: ArrayList<androidx.fragment.app.Fragment> = arrayListOf()

    private val datas: ArrayList<MockData> = arrayListOf()


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TabLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)

        for (i in 0 until 10) {
            datas.add(MockData("标题$i"))
            fragments.add(TabLayoutFragment.newInstance("第${i}个fragment"))
        }
        datas[1].resId = R.drawable.balloon

        for (i in 0 until 10) {
            val tab: TabLayout.Tab
            val mockData = datas[i]
            if (mockData.resId == -1) {
                val view = LayoutInflater.from(this).inflate(R.layout.custom_tablayout_text, null)
                view.findViewById<TextView>(R.id.tvText).setText(mockData.text)
                tab = tabLayout.newTab().setCustomView(view)
            } else {
                val view = LayoutInflater.from(this).inflate(R.layout.custom_tablayout_image, null)
                view.findViewById<ImageView>(R.id.ivImage).setImageResource(mockData.resId)
                tab = tabLayout.newTab().setCustomView(view)
            }
            tabLayout.addTab(tab)
        }

        viewPager.adapter = object : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): androidx.fragment.app.Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(p0: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                //得到当前选中的tab的位置，切换相应的fragment
                val nowPosition: Int = tab.position
                viewPager.currentItem = nowPosition
            }

        })
        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                //do nothing
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                //do nothing

            }

            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()
            }
        })

    }

    class MockData @JvmOverloads constructor(var text: String, var resId: Int = -1)
}

