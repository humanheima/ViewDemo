package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.RecycleViewAdapter
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.widget.NewNestedScrollView
import java.util.*


/**
 * Created by p_dmweidu on 2023/2/2
 * Desc: 测试监听NestedScrollView滑动停止
 */
class ListenNestedScrollViewActivity : BaseActivity() {

    private var dataList: MutableList<String> = arrayListOf()

    private var adapter: RecycleViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    private var recyclerView: RecyclerView? = null

    private var newNestedScrollView: NewNestedScrollView? = null

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ListenNestedScrollViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun bindLayout(): Int {
        return R.layout.activity_listen_nested_scroll_view
    }

    override fun initData() {
        dataList = ArrayList()
        for (i in 0..20) {
            dataList.add("data$i")
        }
        newNestedScrollView = findViewById(R.id.newNestedScrollView)
        recyclerView = findViewById(R.id.rv_gold_voice_list)
        layoutManager = LinearLayoutManager(this)
        adapter = RecycleViewAdapter(dataList, this)

        adapter?.setOnItemClickListener { view, position ->
            Toast.makeText(this, "item postion = ${position}", Toast.LENGTH_SHORT).show()
        }
        recyclerView?.setLayoutManager(layoutManager)
        recyclerView?.setAdapter(adapter)

        newNestedScrollView?.addScrollChangeListener(object :
            NewNestedScrollView.AddScrollChangeListener {

            override fun onScrollChange(
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {

            }

            override fun onScrollState(state: NewNestedScrollView.ScrollState) {
                when (state) {
                    NewNestedScrollView.ScrollState.DRAG -> {
                        Log.i(TAG, "onScrollState: DRAG")
                    }
                    NewNestedScrollView.ScrollState.SCROLLING -> {
                        Log.i(TAG, "onScrollState: SCROLLING")
                    }
                    NewNestedScrollView.ScrollState.IDLE -> {
                        Log.i(TAG, "onScrollState: IDLE")
                    }
                }
            }
        })

    }
}