package com.hm.viewdemo.guide

import android.content.Context
import android.view.View

/**
 * Created by p_dmweidu on 2025/6/11
 * Desc: 引导对象
 * 1. 也可以提供一个 contentLayoutId，暂时不提供
 */
data class GuideItem(
    val id: String,
    val context: Context,
    val priority: Int = 0,//优先级，相隔10个，避免中间临时插入其他优先级引导
    val viewFinder: () -> View?,//提供锚点View
    val strategyProvider: (Context) -> GuideStrategy, // 策略工厂函数
    val dismissListener: (() -> Unit)? = null
)