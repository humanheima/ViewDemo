package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityTabLayoutBinding

/**
 * Created by dumingwei on 2020/4/28
 *
 * Desc: TabLayout使用
 */
class TabLayoutActivity : BaseActivity<ActivityTabLayoutBinding>() {

    private val fragments: ArrayList<Fragment> = arrayListOf()

    private val datas: ArrayList<MockData> = arrayListOf()

    private var tabLayoutMediator: TabLayoutMediator? = null

    private var btnChangeDataset: Button? = null

    private var adapter: FragmentStateAdapter? = null


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TabLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun createViewBinding(): ActivityTabLayoutBinding {
        return ActivityTabLayoutBinding.inflate(layoutInflater)
    }

    override fun initData() {
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
                tab = binding.tabLayout.newTab().setCustomView(view)
            } else {
                val view = LayoutInflater.from(this).inflate(R.layout.custom_tablayout_image, null)
                view.findViewById<ImageView>(R.id.ivImage).setImageResource(mockData.resId)
                tab = binding.tabLayout.newTab().setCustomView(view)
            }
            binding.tabLayout.addTab(tab)
        }

        adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }


        }

        binding.viewPager2.adapter = adapter


        tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    val mockData = datas[position]
                    if (mockData.resId == -1) {
                        val view = LayoutInflater.from(this@TabLayoutActivity)
                            .inflate(R.layout.custom_tablayout_text, null)
                        view.findViewById<TextView>(R.id.tvText).setText(mockData.text)
                        tab.setCustomView(view)
                        view.background = getDrawable(R.drawable.bg_f96300_radius_23)

                    } else {
                        val view = LayoutInflater.from(this@TabLayoutActivity)
                            .inflate(R.layout.custom_tablayout_image, null)
                        view.findViewById<ImageView>(R.id.ivImage).setImageResource(mockData.resId)
                        tab.setCustomView(view)
                    }
                }

            })

        tabLayoutMediator?.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //do nothing

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //do nothing
                val view = tab?.customView
                if (view is TextView) {
                    view.textSize = 14f
                    view.setTextColor(resources.getColor(R.color.colorPrimary))
                }
                view?.background = getDrawable(R.drawable.bg_f96300_radius_23)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //得到当前选中的tab的位置，切换相应的fragment
                //val nowPosition: Int = tab.position
                //viewPager.currentItem = nowPosition

                val view = tab?.customView
                if (view is TextView) {
                    view.textSize = 20f
                    view.setTextColor(resources.getColor(R.color.colorAccent))
                }
                view?.background = getDrawable(R.drawable.bg_gradient_ffea90_fcbe2d_radius_23)
            }


        })

        binding.tabLayout.selectTab(null)


        btnChangeDataset = findViewById(R.id.btn_change_dataset)

        btnChangeDataset?.setOnClickListener {
            datas.clear()
            fragments.clear()
            for (i in 0 until 3) {
                datas.add(MockData("标题$i"))
                fragments.add(TabLayoutFragment.newInstance("第${i}个fragment"))
            }

            adapter?.notifyDataSetChanged()
            tabLayoutMediator?.detach()
            tabLayoutMediator?.attach()
            binding.tabLayout.selectTab(null)
        }
    }


    class MockData @JvmOverloads constructor(var text: String, var resId: Int = -1)
}

