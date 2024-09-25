package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
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