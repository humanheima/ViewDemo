package com.hm.viewdemo.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.item_recycler_view.view.*

/**
 * Created by dumingwei on 2017/3/5.
 */
class CustomRecyclerViewAdapter(private val context: Context, private val dataList: List<String>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.itemView.text1.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

     class CustomViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    }
}
