package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_nested_scroll_version_two.*
import java.util.*

class NestedScrollVersion2Activity : AppCompatActivity() {

    private val mTitles = arrayOf("简介", "评价", "相关")
    private var mAdapter: FragmentPagerAdapter? = null
    private val mFragments = arrayListOf<TabFragment>()

    private val mDatas = ArrayList<String>()

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NestedScrollVersion2Activity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_version_two)

        initDatas()

        val intent = Intent("")
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
    }

    private fun initDatas() {
        for (i in mTitles.indices) {
            mFragments.add(TabFragment.newInstance(mTitles[i]) as TabFragment)
        }

        mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getItem(position: Int): Fragment {
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
