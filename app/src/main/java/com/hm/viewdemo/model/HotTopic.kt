package com.hm.viewdemo.model

/**
 * 热点话题数据模型
 */
data class HotTopic(
    val id: Int,
    val rank: Int,
    val title: String,
    val viewCount: String,
    val isHot: Boolean = false
)