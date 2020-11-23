package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.RecycleViewAdapter
import com.hm.viewdemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_max_height_layout.*
import java.util.*

class MaxHeightLayoutActivity : BaseActivity() {

    private var popWindow: PopupWindow? = null
    private var muchPopWindow: PopupWindow? = null

    private lateinit var lessDataList: MutableList<String>
    private lateinit var muchDataList: MutableList<String>

    private var lessAdapter: RecycleViewAdapter? = null
    private var muchAdapter: RecycleViewAdapter? = null

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, MaxHeightLayoutActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun bindLayout(): Int {
        return R.layout.activity_max_height_layout
    }

    override fun initData() {
        lessDataList = ArrayList()
        lessDataList.add("三国演义")
        //lessDataList.add("水浒传");
        //lessDataList.add("红楼梦");
        muchDataList = ArrayList()
        for (i in 0..29) {
            muchDataList.add("much data$i")
        }
        lessAdapter = RecycleViewAdapter(lessDataList, this)
        muchAdapter = RecycleViewAdapter(muchDataList, this)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_show_pop_window -> showPop()
            R.id.btn_show_much_pop_window -> showMuchPop()
        }
    }

    private fun showPop() {
        if (popWindow == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.pop, null)
            val recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = lessAdapter
            popWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popWindow!!.setBackgroundDrawable(ColorDrawable())
            popWindow!!.showAtLocation(btn_show_pop_window, Gravity.BOTTOM, 0, 0)
        } else {
            popWindow!!.showAtLocation(btn_show_pop_window, Gravity.BOTTOM, 0, 0)
        }
    }

    private fun showMuchPop() {
        if (muchPopWindow == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.pop, null)
            val recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = muchAdapter
            muchPopWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            muchPopWindow!!.isFocusable = true
            muchPopWindow!!.setBackgroundDrawable(ColorDrawable())
            muchPopWindow!!.showAtLocation(btn_show_much_pop_window, Gravity.BOTTOM, 0, 0)
        } else {
            muchPopWindow!!.showAtLocation(btn_show_much_pop_window, Gravity.BOTTOM, 0, 0)
        }
    }


}