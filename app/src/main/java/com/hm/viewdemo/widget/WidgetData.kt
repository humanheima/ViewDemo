package com.hm.viewdemo.widget

/**
 * 桌面小组件展示数据模型
 */
data class WidgetData(
    val title: String,
    val brandName: String,
    val characters: List<Character>
) {
    data class Character(
        val name: String,
        val avatarUrl: String
    )
}
