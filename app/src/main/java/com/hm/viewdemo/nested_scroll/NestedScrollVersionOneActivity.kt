package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityNestedScrollVersionOneBinding

/**
 * Crete by dumingwei on 2020-02-15
 * Desc: 学习嵌套滑动
 *
 */
class NestedScrollVersionOneActivity : BaseActivity<ActivityNestedScrollVersionOneBinding>() {

    private val mTitles = arrayOf("简介", "评价", "相关")
    private var mAdapter: FragmentPagerAdapter? = null
    private val mFragments = arrayListOf<TabFragment>()


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NestedScrollVersionOneActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityNestedScrollVersionOneBinding {
        return ActivityNestedScrollVersionOneBinding.inflate(layoutInflater)
    }

    override fun initData() {
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

            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }

        }
        binding.stickyNavLayoutViewpager.adapter = mAdapter
        binding.stickyNavLayoutIndicator.setupWithViewPager(binding.stickyNavLayoutViewpager)
    }

}
