package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_nested_scroll_version_one.*

/**
 * Crete by dumingwei on 2020-02-15
 * Desc: 学习嵌套滑动
 *
 */
class NestedScrollVersionOneActivity : AppCompatActivity() {

    private val mTitles = arrayOf("简介", "评价", "相关")
    private var mAdapter: androidx.fragment.app.FragmentPagerAdapter? = null
    private val mFragments = arrayListOf<TabFragment>()


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NestedScrollVersionOneActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_version_one)
        initDatas()
        val intent = Intent("")
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
    }

    private fun initDatas() {
        for (i in mTitles.indices) {
            mFragments.add(TabFragment.newInstance(mTitles[i]) as TabFragment)
        }

        mAdapter = object : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getItem(position: Int): androidx.fragment.app.Fragment {
                return mFragments[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }

        }
        stickyNavLayoutViewpager.adapter = mAdapter
        stickyNavLayoutIndicator.setupWithViewPager(stickyNavLayoutViewpager)
    }

}
