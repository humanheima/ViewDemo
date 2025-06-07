package com.hm.viewdemo.custom_view.chapter7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.custom_view.chapter7.TextAdapter.TextViewHolder

class TextAdapter(private val dataList: List<TextModel>) : RecyclerView.Adapter<TextViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_text, parent, false)
        return TextViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val data = dataList[position]
        holder.textView.text = data.text
        holder.textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        holder.textView.setShadowLayer(data.radius, data.dx, data.dy, data.color)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_view)
    }

    data class TextModel(
        val text: String,
        val radius: Float,
        val color: Int,
        val dx: Float,
        val dy: Float
    )
}