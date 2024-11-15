package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.LinearLayout

/**
 * Created by p_dmweidu on 2024/11/15
 * Desc: 定义监听左右滑动的LinearLayout
 */
class CustomLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val TAG = "CustomLinearLayout"

    private val swipeVelocityThreshold = 1000 // 速度阈值
    private var velocityTracker: VelocityTracker? = null
    private var initialX = 0f // 初始 X 坐标
    private var isSwiping = false // 是否正在滑动

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(ev)

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = ev.x // 记录初始 X 坐标
                isSwiping = false // 重置滑动状态
                velocityTracker?.clear()
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.x - initialX
                velocityTracker?.computeCurrentVelocity(1000) // 计算速度

                val xVelocity = velocityTracker?.xVelocity ?: 0f

                // 判断滑动方向和速度
                if (Math.abs(xVelocity) > swipeVelocityThreshold) {
                    isSwiping = true
                    if (deltaX > 0) {
                        Log.d(TAG, "Swiped Right with velocity: $xVelocity")
                    } else {
                        Log.d(TAG, "Swiped Left with velocity: $xVelocity")
                    }
                    return true // 拦截事件
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                velocityTracker?.recycle()
                velocityTracker = null
            }
        }

        return super.onInterceptTouchEvent(ev) // 不拦截，交给子视图处理
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG, "onTouchEvent: event = $event")
        return super.onTouchEvent(event)
    }

}