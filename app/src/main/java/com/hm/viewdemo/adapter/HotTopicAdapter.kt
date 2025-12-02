package com.hm.viewdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.model.HotTopic

class HotTopicAdapter(private val hotTopics: List<HotTopic>) :
    RecyclerView.Adapter<HotTopicAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRank: TextView = itemView.findViewById(R.id.tv_rank)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val ivHotIcon: ImageView = itemView.findViewById(R.id.iv_hot_icon)
        val tvViewCount: TextView = itemView.findViewById(R.id.tv_view_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hot_topic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = hotTopics[position]
        
        holder.tvRank.text = topic.rank.toString()
        holder.tvTitle.text = topic.title
        holder.tvViewCount.text = topic.viewCount
        
        // 设置排名颜色
        val rankColor = when (topic.rank) {
            1 -> ContextCompat.getColor(holder.itemView.context, R.color.rank_first)
            2 -> ContextCompat.getColor(holder.itemView.context, R.color.rank_second)  
            3 -> ContextCompat.getColor(holder.itemView.context, R.color.rank_third)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.rank_others)
        }
        holder.tvRank.setTextColor(rankColor)
        
        // 显示热门标识
        holder.ivHotIcon.visibility = if (topic.isHot) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = hotTopics.size
}