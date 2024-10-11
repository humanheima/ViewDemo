package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.hm.viewdemo.adapter.RecycleViewAdapter
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityCoordinateLayoutBinding


/**
 * Created by p_dmweidu on 2024/9/25
 * Desc: 学习协调布局，相关的 behavior 等
 */
class CoordinateLayoutActivity : BaseActivity<ActivityCoordinateLayoutBinding>() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, CoordinateLayoutActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityCoordinateLayoutBinding {
        return ActivityCoordinateLayoutBinding.inflate(layoutInflater)
    }

    override fun initData() {
        setRvAdapter()

        val appBarLayout = binding.appbarLayout

        val behavior: AppBarLayout.Behavior = TestScrollBehavior()
        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = behavior
        appBarLayout.layoutParams = params

        binding.recyclerView.postDelayed({

            val appBarLp = binding.appbarLayout.layoutParams as CoordinatorLayout.LayoutParams

            Log.d(TAG, "appBarLp behavior = ${appBarLp.behavior}")

            val lp = binding.recyclerView.layoutParams as CoordinatorLayout.LayoutParams

            Log.d(TAG, "initData: behavior = ${lp.behavior}")
            Log.d(TAG, "initData: binding.recyclerView height = ${binding.recyclerView.height}")
        }, 100)

    }

    private fun setRvAdapter() {
        val stringList = ArrayList<String>()
        for (i in 0 until 30) {
            stringList.add("string$i")
        }
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecycleViewAdapter(stringList, this)
        binding.recyclerView.adapter = adapter


    }

}