package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.bean.LiteChildTab
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.LiteSingleChildTabView

/**
 * Created by dumingwei on 2021/6/23
 *
 * Desc:
 */
class ChildTabTestActivity : AppCompatActivity() {


    private val TAG: String = "ChildTabTestActivity"

    private var dp60: Int = 0
    private var dp50: Int = 0
    private var dp30: Int = 0

    private var dp33: Int = 0

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ChildTabTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var childTabView0: LiteSingleChildTabView
    lateinit var childTabView1: LiteSingleChildTabView
    lateinit var childTabView2: LiteSingleChildTabView
    lateinit var childTabView3: LiteSingleChildTabView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_tab_test)

        dp60 = ScreenUtil.dpToPx(this, 60)
        dp50 = ScreenUtil.dpToPx(this, 50)
        dp30 = ScreenUtil.dpToPx(this, 30)

        dp33 = ScreenUtil.dpToPx(this, 33)

        childTabView0 = findViewById(R.id.main_single_child_tabview_0)
        childTabView1 = findViewById(R.id.main_single_child_tabview_1)
        childTabView2 = findViewById(R.id.main_single_child_tabview_2)
        childTabView3 = findViewById(R.id.main_single_child_tabview_3)


        val list = arrayListOf<LiteChildTab>()

        for (i in 0 until 4) {
            val childTab = LiteChildTab(i.toLong(), "tab$i", false)
            list.add(childTab)
        }
        list[0].selectd = true

        bindAverageViews(list.size, list)

    }

    private fun bindAverageViews(size: Int, tabList: List<LiteChildTab>) {
        //使用线性布局均分
        val viewList = arrayListOf<LiteSingleChildTabView>()

        when (size) {
            2 -> {
                childTabView0.visibility = View.VISIBLE
                childTabView1.visibility = View.VISIBLE

                childTabView2.visibility = View.GONE
                childTabView3.visibility = View.GONE

                //修改间距
                val layoutParams1 = childTabView1.layoutParams as LinearLayout.LayoutParams
                layoutParams1.marginStart = dp60
                childTabView1.layoutParams = layoutParams1

                val drawable0 = resources?.getDrawable(R.drawable.main_bg_gradient_7bcfff_ebf8ff_radius_4)
                childTabView0.setData(tabList[0], drawable0)

                val drawable1 = resources?.getDrawable(R.drawable.main_bg_gradient_fd639d_ffd8e7_radius_4)
                childTabView1.setData(tabList[1], drawable1)

                //childTabView0.setSelectedStatus(false)
                //childTabView1.setSelectedStatus(false)

                viewList.add(childTabView0)
                viewList.add(childTabView1)
            }
            3 -> {
                childTabView0.visibility = View.VISIBLE
                childTabView1.visibility = View.VISIBLE
                childTabView2.visibility = View.VISIBLE

                childTabView3.visibility = View.GONE

                //修改间距
                val layoutParams1 = childTabView1.layoutParams as LinearLayout.LayoutParams
                layoutParams1.marginStart = dp50
                childTabView1.layoutParams = layoutParams1

                val layoutParams2 = childTabView2.layoutParams as LinearLayout.LayoutParams
                layoutParams2.marginStart = dp50
                childTabView2.layoutParams = layoutParams2

                val drawable = resources?.getDrawable(R.drawable.main_bg_gradient_ffd9dc_fa696f_radius_4)

                childTabView0.setData(tabList[0], drawable)
                childTabView1.setData(tabList[1], drawable)
                childTabView2.setData(tabList[2], drawable)

                //childTabView0.setSelectedStatus(false)

                //childTabView1.setSelectedStatus(false)
                //childTabView2.setSelectedStatus(false)

                viewList.add(childTabView0)
                viewList.add(childTabView1)
                viewList.add(childTabView2)

            }
            else -> {
                childTabView0.visibility = View.VISIBLE
                childTabView1.visibility = View.VISIBLE
                childTabView2.visibility = View.VISIBLE
                childTabView3.visibility = View.VISIBLE

                //修改间距
//                val layoutParams1 = childTabView1.layoutParams as LinearLayout.LayoutParams
//                layoutParams1.marginStart = dp30
//                childTabView1.layoutParams = layoutParams1
//
//                val layoutParams2 = childTabView2.layoutParams as LinearLayout.LayoutParams
//                layoutParams2.marginStart = dp30
//                childTabView2.layoutParams = layoutParams2
//
//                val layoutParams3 = childTabView3.layoutParams as LinearLayout.LayoutParams
//                layoutParams3.marginStart = dp30
//                childTabView3.layoutParams = layoutParams3

                val drawable = resources?.getDrawable(R.drawable.main_bg_gradient_ffd9dc_fa696f_radius_4)

                childTabView0.setData(tabList[0], drawable)
                childTabView1.setData(tabList[1], drawable)
                childTabView2.setData(tabList[2], drawable)
                childTabView3.setData(tabList[3], drawable)

                viewList.add(childTabView0)
                viewList.add(childTabView1)
                viewList.add(childTabView2)
                viewList.add(childTabView3)

            }
        }

        if (viewList.size == size) {
            for (i in tabList.indices) {
                if (tabList[i].selectd) {
                    viewList[i].setSelectedStatus(true)
                } else {
                    viewList[i].setSelectedStatus(false)
                }
            }
        }

        childTabView0.post {
            Log.i(TAG, "bindAverageViews: ${childTabView0.height} , dp33 = $dp33")
        }

        childTabView0.setOnClickListener {
            //已经选中，不再做操作
            if (childTabView0.getData()?.selectd == true) {
                return@setOnClickListener
            }
            childTabView0.setSelectedStatus(true)

            childTabView1.setSelectedStatus(false)
            if (childTabView2.visibility == View.VISIBLE) {
                childTabView2.setSelectedStatus(false)
            }
            if (childTabView3.visibility == View.VISIBLE) {
                childTabView3.setSelectedStatus(false)
            }
        }
        childTabView1.setOnClickListener {
            if (childTabView1.getData()?.selectd == true) {
                return@setOnClickListener
            }
            childTabView0.setSelectedStatus(false)
            childTabView1.setSelectedStatus(true)

            if (childTabView2.visibility == View.VISIBLE) {
                childTabView2.setSelectedStatus(false)
            }
            if (childTabView3.visibility == View.VISIBLE) {
                childTabView3.setSelectedStatus(false)
            }
        }
        childTabView2.setOnClickListener {
            if (childTabView2.getData()?.selectd == true) {
                return@setOnClickListener
            }
            childTabView0.setSelectedStatus(false)
            childTabView1.setSelectedStatus(false)
            childTabView2.setSelectedStatus(true)

            if (childTabView3.visibility == View.VISIBLE) {
                childTabView3.setSelectedStatus(false)
            }
        }
        childTabView3.setOnClickListener {
            if (childTabView3.getData()?.selectd == true) {
                return@setOnClickListener
            }
            childTabView0.setSelectedStatus(false)
            childTabView1.setSelectedStatus(false)
            childTabView2.setSelectedStatus(false)

            childTabView3.setSelectedStatus(true)

        }
    }

}