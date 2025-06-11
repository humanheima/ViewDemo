package com.hm.viewdemo.guide

import android.util.Log
import android.view.View
import androidx.annotation.MainThread
import java.util.TreeSet

/**
 * Created by p_dmweidu on 2025/6/11
 * Desc: 引导管理类
 * 1. 请在主线程调用
 */
@MainThread
object GuideQueueManager {

    private const val TAG = "GuideQueueManager"

    private val lock = Any()

    private var isProcessing = false

    private val itemTreeSet = TreeSet<GuideItem>(compareByDescending { it.priority })
    private var currentStrategy: GuideStrategy? = null
    private var currentGuideItem: GuideItem? = null

    fun addGuide(guideItem: GuideItem) {
        itemTreeSet.add(guideItem)
        if (!isProcessing) {
            isProcessing = true
            tryShowNextGuide()
            Log.d(TAG, "addGuide: 调用 tryShowNextGuide")
        }
    }

    private fun tryShowNextGuide() {
        if (itemTreeSet.isEmpty()) {
            isProcessing = false
            return
        }
        if (currentStrategy != null) {
            //正在展示，直接返回
            isProcessing = false
            return
        }

        Log.d(TAG, "tryShowNextGuide: itemTreeSet.size = ${itemTreeSet.size}")
        //获取并移除
        val guideItem = itemTreeSet.pollFirst()

        Log.d(TAG, "tryShowNextGuide: itemTreeSet.size = ${itemTreeSet.size}")


        val context = guideItem?.context
        val targetView: View? = guideItem?.viewFinder?.invoke()

        //不合法直接展示下一个
        if (guideItem == null || context == null || targetView == null) {
            tryShowNextGuide()
            return
        }

        val strategy = guideItem.strategyProvider(context)
        currentStrategy = strategy
        currentGuideItem = guideItem

        strategy.show(targetView, guideItem) {
            dismissCurrentGuide()
        }
    }

    fun dismissCurrentGuide() {
        currentStrategy?.dismiss()
        currentStrategy = null
        currentGuideItem?.dismissListener?.invoke()
        currentGuideItem = null
        tryShowNextGuide()
    }

    /**
     * 清空了 itemTreeSet
     * dismissCurrentGuide 方法内部发现 itemTreeSet.isEmpty()，会直接返回
     */
    fun clearAllGuides() {
        itemTreeSet.clear()
        dismissCurrentGuide()
    }
}