package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.item_xfermode.view.*

/**
 * Created by dumingwei on 2017/3/5.
 */
class XfermodeAdapter(private val context: Context, private val dataList: List<PorterDuff.Mode>)
    : RecyclerView.Adapter<XfermodeAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_xfermode, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = dataList[position]
        //holder.itemView.xfermodeView.setXfermode(model)
        holder.itemView.tvMode.text = model.name
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
}

