package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ItemXfermodeBinding

/**
 * Created by dumingwei on 2017/3/5.
 */
class XfermodeAdapter(private val context: Context, private val dataList: List<PorterDuff.Mode>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<XfermodeAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_xfermode, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = dataList[position]
        //holder.itemView.xfermodeView.setXfermode(model)
        holder.binding.tvMode.text = model.name
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class VH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val binding = ItemXfermodeBinding.bind(itemView)
    }
}

