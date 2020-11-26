package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.support.v4.view.NestedScrollingParent2
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2020-02-15.
 * Desc:
 */
class StickyNavLayout2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), NestedScrollingParent2 {

    private val TAG: String = "StickyNavLayout2"

    private lateinit var mTop: View
    private lateinit var mNav: View
    private lateinit var mViewPager: ViewPager

    private var mTopViewHeight: Int = 0

    private val mNestedScrollingParentHelper: NestedScrollingParentHelper = NestedScrollingParentHelper(this)

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

        Log.e(TAG, "onMeasure1: measuredHeight = $measuredHeight , mNav.measuredHeight = ${mNav.measuredHeight} , mViewPager.measuredHeight = ${mViewPager?.measuredHeight}")

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
        Log.e(TAG, "onMeasure2: measuredHeight = $measuredHeight , mNav.measuredHeight = ${mNav.measuredHeight} , mViewPager.measuredHeight = ${mViewPager?.measuredHeight}")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTopViewHeight = mTop.measuredHeight
        Log.e(TAG, "onSizeChanged: mTopViewHeight =$mTopViewHeight")
    }


    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return (nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes)
    }

    override fun getNestedScrollAxes(): Int {
        return mNestedScrollingParentHelper.nestedScrollAxes
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        Log.e(TAG, "ViewGroup onNestedPreScroll:  dx=$dx , dy=$dy")
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

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        //do nothing
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return false
    }

    override fun onStopNestedScroll(child: View) {
        mNestedScrollingParentHelper.onStopNestedScroll(child)
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


    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.e(TAG, "onStartNestedScroll: ")
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.e(TAG, "NestedScrollingParent2 onNestedScrollAccepted: ")
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.e(TAG, "NestedScrollingParent2 onNestedPreScroll:  dx=$dx , dy=$dy")
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

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        Log.e(TAG, "NestedScrollingParent2 onNestedScroll: ")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.e(TAG, "NestedScrollingParent2 onStopNestedScroll: ")
        mNestedScrollingParentHelper.onStopNestedScroll(target, type)
    }

}