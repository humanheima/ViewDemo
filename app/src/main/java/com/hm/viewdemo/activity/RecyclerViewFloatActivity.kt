package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.ListViewAdapter
import com.hm.viewdemo.bean.MyBean

/**
 * Created by dumingwei on 2020/4/1
 *
 * Desc: ListView的item的悬浮效果
 */
class RecyclerViewFloatActivity : AppCompatActivity() {


    private lateinit var list: ArrayList<MyBean>
    private lateinit var adapter: ListViewAdapter

    private val TAG: String? = "ListViewFloatActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, RecyclerViewFloatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_float)
    }

    private fun addFloatView() {
        val floatView = View.inflate(this, R.layout.view_float_layout, null)
        floatView.findViewById<ImageView>(R.id.ivFloatImage).setOnClickListener {
            Toast.makeText(this, "做为ListView的item中的控件", Toast.LENGTH_SHORT).show()
        }

    }

    private fun useArrayAdapter() {
        list = arrayListOf()
        for (i in 0..20) {
            val bean = MyBean("title$i", "detail$i")
            list.add(bean)
        }
        adapter = ListViewAdapter(this, R.layout.item_list_view, list)

    }

}
