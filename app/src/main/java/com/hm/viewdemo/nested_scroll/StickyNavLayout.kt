package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.NestedScrollingParent
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.OverScroller
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2020-02-15.
 * Desc:
 */
class StickyNavLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), NestedScrollingParent {


    private val TAG = "StickyNavLayout"

    private val TOP_CHILD_FLING_THRESHOLD = 0

    private lateinit var mTop: View
    private lateinit var mNav: View
    private lateinit var mViewPager: ViewPager

    private var mTopViewHeight: Int = 0

    private var mScroller: OverScroller = OverScroller(context)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTop = findViewById(R.id.stickyNavLayoutTopView)
        mNav = findViewById(R.id.stickyNavLayoutIndicator)
        val view: View = findViewById(R.id.stickyNavLayoutViewpager)
        if (view !is ViewPager) {
            throw RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !")
        }
        mViewPager = view
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 先测量一遍，获取自身的高度和子控件的高度
         */
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Log.d(TAG, "onMeasure1: measuredHeight = $measuredHeight , mNav.measuredHeight = ${mNav.measuredHeight} , mViewPager.measuredHeight = ${mViewPager?.measuredHeight}")

        /**
         * 这里为什么要将 mViewPager高度设置为mViewPager原本的高度加上mNav的高度呢？因为mTop滑出去以后，mNav和mViewpager应该占据
         * StickyNavLayout的整个高度，不然StickyNavLayout底部会有空白。
         */
        val params = mViewPager.layoutParams
        params?.height = measuredHeight - mNav.measuredHeight

        /**
         * 子控件mViewPager的高度发生了变化，重新测量一遍，最终的结果是该控件本身高度没有发生变化，但是子控件mViewPager的变高了
         */
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure2: measuredHeight = $measuredHeight , mNav.measuredHeight = ${mNav.measuredHeight} , mViewPager.measuredHeight = ${mViewPager?.measuredHeight}")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTopViewHeight = mTop.measuredHeight
        Log.d(TAG, "onSizeChanged: mTopViewHeight =$mTopViewHeight")
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        Log.d(TAG, "onStartNestedScroll: nestedScrollAxes = $nestedScrollAxes")
        return (nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        Log.d(TAG, "onNestedScrollAccepted: ")
    }

    override fun onStopNestedScroll(child: View) {
        Log.d(TAG, "onStopNestedScroll: ")
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.d(TAG, "onNestedScroll: ")
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.d(TAG, "onNestedPreScroll:  dx=$dx , dy=$dy")

        /**
         * scrollY>=0，向上滑动的时候，scrollY增加。
         */
        // 向上滑动（手指从下向上滑）, dy>0
        val hiddenTop = dy > 0 && scrollY < mTopViewHeight

        // dy<0 向下滑动（手指从上向下滑）
        val showTop = dy < 0 && scrollY >= 0 && !target.canScrollVertically(-1)
        if (hiddenTop || showTop) {
            //滑动y距离
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {

        Log.d(TAG, "onNestedPreFling velocityY = $velocityY  scrollY = $scrollY  mTop.height = ${mTop.height}")

        /**
         * 向上滑动（手指从下向上滑）, velocityY>0
         * 向下滑动（手指从上向下滑）, velocityY<0
         */
        if (velocityY > 0) {
            if (scrollY < mTop.height) {
                fling(velocityY.toInt())
                return true
            }
        } else {
            var shouldFling = false//该控件是否应该惯性滑动

            if (target is RecyclerView && velocityY < 0) {

                val firstChild = target.getChildAt(0)
                val childAdapterPosition = target.getChildAdapterPosition(firstChild)
                Log.d(TAG, "onNestedPreFling: childAdapterPosition = $childAdapterPosition")
                /**
                 * 向下滑动的时候，如果RecyclerView中第一个可见item的位置在adapter中的位置等于0，
                 * 则认为RecyclerView自己消费了fling事件，否则认为RecyclerView没有消费
                 */
                shouldFling = (childAdapterPosition == TOP_CHILD_FLING_THRESHOLD)

            }
            if (shouldFling) {
                fling(velocityY.toInt())
                return true
            }
        }

        return false
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return false
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout: ")
        super.onLayout(changed, l, t, r, b)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw: ")
        super.onDraw(canvas)
    }

    private fun fling(velocityY: Int) {
        mScroller.fling(0, scrollY, 0, velocityY, 0, 0, 0, mTopViewHeight)
        invalidate()
    }

    override fun scrollTo(x: Int, y: Int) {
        val distanceY = if (y < 0) {
            0
        } else {
            if (y > mTopViewHeight) {
                mTopViewHeight
            } else {
                y
            }
        }

        if (distanceY != scrollY) {
            super.scrollTo(x, distanceY)
        }
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val y = mScroller.currY
            Log.d(TAG, "computeScroll: mScroller.currY = $y")
            scrollTo(0, y)
            invalidate()
        }
    }

}