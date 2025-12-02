package com.hm.viewdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.model.Discussion
import de.hdodenhof.circleimageview.CircleImageView

class DiscussionAdapter(private val discussions: List<Discussion>) :
    RecyclerView.Adapter<DiscussionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: CircleImageView = itemView.findViewById(R.id.iv_avatar)
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        val tvCommentCount: TextView = itemView.findViewById(R.id.tv_comment_count)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tv_like_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_discussion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val discussion = discussions[position]
        
        holder.ivAvatar.setImageResource(discussion.avatar)
        holder.tvUsername.text = discussion.username
        holder.tvTime.text = discussion.timeAgo
        holder.tvContent.text = discussion.content
        holder.tvCommentCount.text = discussion.commentCount.toString()
        holder.tvLikeCount.text = discussion.likeCount.toString()
    }

    override fun getItemCount(): Int = discussions.size
}