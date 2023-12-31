package com.hm.viewdemo.widget.scroll_view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.OverScroller
import kotlin.math.abs

/**
 * Created by p_dmweidu on 2023/12/31
 * Desc: 测试OverScroller 的用法
 */
class SimpleOverScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val TAG = "SimpleOverScrollView"

    private var mLastTouchY = 0

    private var scroller: OverScroller = OverScroller(context)

    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0
    private var mVelocityTracker: VelocityTracker = VelocityTracker.obtain()

    private var mTouchSlop = 0

    init {
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i(TAG, "onMeasure:  measuredHeight = $measuredHeight")
    }

    /**
     * 要重写此方法，使用MeasureSpec.UNSPECIFIED的方式测量子View
     */
    override fun measureChild(
        child: View,
        parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ) {
        val lp = child.layoutParams
        val childHeightMeasureSpec: Int
        val childWidthMeasureSpec: Int = ViewGroup.getChildMeasureSpec(
            parentWidthMeasureSpec, paddingLeft
                    + paddingRight, lp.width
        )
        val verticalPadding = paddingTop + paddingBottom
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - verticalPadding),
            MeasureSpec.UNSPECIFIED
        )
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    /**
     * 要重写此方法，使用MeasureSpec.UNSPECIFIED的方式测量子View
     */
    override fun measureChildWithMargins(
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
            parentWidthMeasureSpec,
            paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin
                    + widthUsed, lp.width
        )
        val usedTotal = paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin +
                heightUsed
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotal),
            MeasureSpec.UNSPECIFIED
        )
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchY = y
                intercepted = false
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                mLastTouchY = y
                return true
            }

            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
                mLastTouchY = y
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaY = mLastTouchY - y
                Log.e(TAG, "onTouchEvent: scrollY = $scrollY deltaY = $deltaY  height = $height")
                //scrollTo(0, scrollY + deltaY)
                mLastTouchY = y
                overScrollBy(0, deltaY, 0, scrollY, 0, getScrollRange(), 0, 400, true)
            }

            MotionEvent.ACTION_UP -> {
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity * 1.0f)
                val initialVelocity = mVelocityTracker.yVelocity.toInt()

                //fling
                if (abs(initialVelocity) > mMinimumVelocity) {
                    Log.e(TAG, "onTouchEvent: fling initialVelocity =$initialVelocity")
                    fling(-initialVelocity)
                }
                mVelocityTracker.clear()
            }

            else -> {
            }
        }
        return true
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        Log.i(TAG, "onOverScrolled: clampedY = $clampedY")
        scrollTo(scrollX, scrollY)
        if (clampedY) {
            scroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())
        }
    }

    private fun fling(velocityY: Int) {
        scroller.fling(0, scrollY, 0, velocityY, 0, 0, 0, getScrollRange(), 0, 100)
        invalidate()
    }

    /**
     *
     */
    override fun computeScroll() {
        Log.i(TAG, "computeScroll: ")
        if (scroller.computeScrollOffset()) {
            val y = scroller.currY
            scrollTo(0, y)
            Log.i(TAG, "computeScroll: mScroller.currY = $y")
            invalidate()
        }
    }

    private fun getScrollRange(): Int {
        var scrollRange = 0
        if (childCount > 0) {
            val child = getChildAt(0)
            val contentHeight = child.height
            val selfHeight = height - paddingBottom - paddingTop
            scrollRange = 0.coerceAtLeast(contentHeight - selfHeight)
            Log.i(
                TAG,
                "getScrollRange:  contentHeight = $contentHeight selfHeight = $selfHeight srcollRange = $scrollRange"
            )
//            Log.i(
//                TAG,
//                "getScrollRange: scrollRange = $scrollRange  自身偏移量 = $scrollY $child 的偏移量 = ${child.scrollY}"
//            )
        }

        return scrollRange
    }
}