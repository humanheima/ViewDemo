package com.hm.viewdemo.guide

import android.view.View

/**
 * Created by p_dmweidu on 2025/6/11
 * Desc: 基础引导策略
 */
interface GuideStrategy {
    /**
     * 检查是否应该显示引导
     */
    fun shouldShow(): Boolean

    /**
     * 执行引导展示逻辑
     * @param anchorView 目标View
     * @param dismissCallback 引导消失时的回调
     */
    fun show(anchorView: View, guideItem: GuideItem, dismissCallback: () -> Unit)

    /**
     * 隐藏引导
     */
    fun dismiss()

    /**
     * 引导成功显示后调用（用于记录状态等）
     */
    fun afterShown()
}