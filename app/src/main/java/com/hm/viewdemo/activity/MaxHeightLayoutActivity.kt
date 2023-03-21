package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.RecycleViewAdapter
import com.hm.viewdemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_max_height_layout.*

class MaxHeightLayoutActivity : BaseActivity() {

    private var popWindow: PopupWindow? = null
    private var muchPopWindow: PopupWindow? = null

    private lateinit var lessDataList: MutableList<String>
    private lateinit var muchDataList: MutableList<String>

    private var lessAdapter: RecycleViewAdapter? = null
    private var muchAdapter: RecycleViewAdapter? = null

    private lateinit var btnTwo: Button

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
        btnTwo = findViewById(R.id.btn_show_much_pop_window)
        btn_show_pop_window.post {
            val location = IntArray(2)
            btn_show_pop_window.getLocationOnScreen(location)
            Log.i(TAG, "initData: ${location[1]}")

        }

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
            val recyclerView =
                view.findViewById<View>(R.id.recycler_view) as androidx.recyclerview.widget.RecyclerView
            val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = lessAdapter
            popWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popWindow!!.setBackgroundDrawable(ColorDrawable())
            popWindow!!.showAsDropDown(btn_show_pop_window, 0, 100, Gravity.BOTTOM)
        } else {
            popWindow!!.showAsDropDown(btn_show_pop_window, 0, 100, Gravity.BOTTOM)
        }
    }

    private fun showMuchPop() {
        if (muchPopWindow == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.pop, null)
            val recyclerView =
                view.findViewById<View>(R.id.recycler_view) as androidx.recyclerview.widget.RecyclerView
            val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = muchAdapter
            muchPopWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            muchPopWindow!!.isFocusable = true
            muchPopWindow!!.setBackgroundDrawable(ColorDrawable())
            muchPopWindow!!.showAtLocation(
                btn_show_much_pop_window,
                Gravity.BOTTOM,
                0,
                btnTwo.height
            )
        } else {
            muchPopWindow!!.showAtLocation(
                btn_show_much_pop_window,
                Gravity.BOTTOM,
                0,
                btnTwo.height
            )
        }
    }


}