package com.hm.viewdemo.activity.design

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_coordinate_layout.*
import kotlinx.android.synthetic.main.item_news_list.view.*


class CoordinateLayoutActivity : AppCompatActivity() {


    private val TAG = "CoordinateLayoutActivit"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CoordinateLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.hm.viewdemo.R.layout.activity_coordinate_layout)
        img_top.setOnClickListener {
            val curTranslationY = app_bar.translationY
            Log.i(TAG, "curTranslationY: $curTranslationY")
            val oa = ObjectAnimator.ofFloat(app_bar, "translationY", curTranslationY, 300f)
            oa.duration = 2000
            oa.start()
        }
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = RvAdapter()
    }


    class RvAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<RvAdapter.VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, position: Int): VH {
            val view = LayoutInflater.from(parent.context).inflate(com.hm.viewdemo.R.layout.item_news_list, parent, false)
            return VH(view)
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.itemView.text_news.text = "hello world$position"
        }


        class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
    }

}
