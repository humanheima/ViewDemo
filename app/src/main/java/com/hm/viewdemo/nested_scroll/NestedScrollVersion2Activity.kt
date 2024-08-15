package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.content.Intent
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityNestedScrollVersionTwoBinding

class NestedScrollVersion2Activity : BaseActivity<ActivityNestedScrollVersionTwoBinding>() {

    private val mTitles = arrayOf("简介", "评价", "相关")
    private var mAdapter: androidx.fragment.app.FragmentPagerAdapter? = null
    private val mFragments = arrayListOf<TabFragment>()

    private val mDatas = ArrayList<String>()

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NestedScrollVersion2Activity::class.java)
            context.startActivity(intent)
        }
    }


    override fun createViewBinding(): ActivityNestedScrollVersionTwoBinding {
        return ActivityNestedScrollVersionTwoBinding.inflate(layoutInflater)
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

            override fun getItem(position: Int): androidx.fragment.app.Fragment {
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
