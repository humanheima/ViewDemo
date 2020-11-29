package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Scroller
import kotlin.math.abs

/**
 * Created by dumingwei on 2020/11/27.
 *
 * Desc:
 */
class SimpleScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TAG: String = "SimpleScrollView"

    private var mLastX = 0
    private var mLastY = 0

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0

    private var scroller: Scroller = Scroller(context)

    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0
    private var mVelocityTracker: VelocityTracker = VelocityTracker.obtain()

    init {
        val configuration = ViewConfiguration.get(context)
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    /**
     * 要重写此方法，使用MeasureSpec.UNSPECIFIED的方式测量子View
     */
    override fun measureChild(child: View, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        val lp = child.layoutParams
        val childWidthMeasureSpec: Int
        val childHeightMeasureSpec: Int
        childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, paddingLeft
                + paddingRight, lp.width)
        val verticalPadding = paddingTop + paddingBottom
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - verticalPadding),
                MeasureSpec.UNSPECIFIED)
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    /**
     * 要重写此方法，使用MeasureSpec.UNSPECIFIED的方式测量子View
     */
    override fun measureChildWithMargins(child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int) {
        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec,
                paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width)
        val usedTotal = paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin +
                heightUsed
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotal),
                MeasureSpec.UNSPECIFIED)
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastXIntercept
                val deltaY = y - mLastYIntercept
                Log.d(TAG, "Math.abs(deltaX)=" + abs(deltaX) + ",Math.abs(deltaY)=" + abs(deltaY))
                intercepted = abs(deltaY) > abs(deltaX)
            }
            MotionEvent.ACTION_UP -> intercepted = false
        }
        Log.d(TAG, "intercepted=$intercepted")
        mLastX = x
        mLastY = y
        mLastXIntercept = x
        mLastYIntercept = y
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (!scroller.isFinished) {
                scroller.abortAnimation()
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = mLastY - y
                Log.e(TAG, "onTouchEvent: scrollY = $scrollY deltaY = $deltaY")
                scrollTo(0, scrollY + deltaY)
                //invalidate()
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
        mLastX = x
        mLastY = y
        return true
    }

    private fun fling(velocityY: Int) {
        scroller.fling(0, scrollY, 0, velocityY, 0, 0, 0, 100000)
        invalidate()
    }

    override fun scrollTo(x: Int, y: Int) {
        var newScrollY = y
        Log.d(TAG, "scrollTo: y= $y")

        if (newScrollY < 0) {
            newScrollY = 0
        }
        val scrollRange = getScrollRange()
        if (newScrollY > scrollRange) {
            newScrollY = scrollRange
        }
        super.scrollTo(x, newScrollY)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            val y = scroller.currY
            Log.d(TAG, "computeScroll: mScroller.currY = $y")
            scrollTo(0, y)
            invalidate()
        }
    }

    private fun getScrollRange(): Int {
        var scrollRange = 0
        if (childCount > 0) {
            val child = getChildAt(0)
            scrollRange = 0.coerceAtLeast(child.height - (height - paddingBottom - paddingTop))
        }
        Log.d(TAG, "getScrollRange: scrollRange = $scrollRange")
        return scrollRange
    }

}