package com.hm.viewdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ItemRecyclerViewBinding

/**
 * Created by dumingwei on 2017/3/5.
 */
class CustomRecyclerViewAdapter(private val context: Context, private val dataList: List<String>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.text1.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemRecyclerViewBinding.bind(itemView)
    }
}
