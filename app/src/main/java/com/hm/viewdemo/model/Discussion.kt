package com.hm.viewdemo.model

/**
 * 讨论数据模型
 */
data class Discussion(
    val id: Int,
    val username: String,
    val avatar: Int, // 头像资源ID
    val timeAgo: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int
)