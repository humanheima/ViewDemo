package com.hm.viewdemo.nested_scroll

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
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
) : LinearLayout(context, attrs, defStyleAttr) {


    private val TAG = "StickyNavLayout"


    private lateinit var mTop: View
    private lateinit var mNav: View
    private lateinit var mViewPager: ViewPager

    private var mTopViewHeight: Int = 0

    private var mOffsetAnimator: ValueAnimator? = null
    var mScroller: OverScroller = OverScroller(context)

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
        Log.d(TAG, "onMeasure: ")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //将mViewPager高度设置为mViewPager原本的高度加上要滑出去的mTop的高度，不然的话，StickyNavLayout底部会有空白
        val params = mViewPager.layoutParams
        params?.height = mTop.measuredHeight + (mViewPager.measuredHeight)
        setMeasuredDimension(measuredWidth,
                (mTop.measuredHeight)
                        + (mNav.measuredHeight)
                        + (mViewPager.measuredHeight)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTopViewHeight = mTop.measuredHeight
        Log.d(TAG, "onSizeChanged: mTopViewHeight =$mTopViewHeight")
    }

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        Log.d(TAG, "onStartNestedScroll: nestedScrollAxes = $nestedScrollAxes")
        return (nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(child: View?, target: View?, axes: Int) {
        super.onNestedScrollAccepted(child, target, axes)
        Log.d(TAG, "onNestedScrollAccepted: ")
    }

    override fun onStopNestedScroll(child: View?) {
        super.onStopNestedScroll(child)
        Log.d(TAG, "onStopNestedScroll: ")
    }

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
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

    private val TOP_CHILD_FLING_THRESHOLD = 3

    /**
     * @param consumed true if the child consumed the fling, false otherwise
     */
    override fun onNestedFling(target: View?, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {

        var childConsumed = consumed

        // 向上滑动（手指从下向上滑）, dy>0
        // 向下滑动（手指从上向下滑）, dy<0
        Log.d(TAG, "onNestedFling: velocityY = $velocityY")
        if (target is RecyclerView && velocityY < 0) {

            val firstChild = target.getChildAt(0)
            val childAdapterPosition = target.getChildAdapterPosition(firstChild)

            /*
             * 向下滑动的时候，如果RecyclerView中第一个item的位置在adapter中的位置大于3，
             * 则认为RecyclerView自己消费了fling事件，否则认为RecyclerView没有消费
             */
            childConsumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD
        }

        //fling(velocityY.toInt())

        if (!childConsumed) {
            animateScroll(velocityY, computeDuration(0f), childConsumed)
        } else {
            animateScroll(velocityY, computeDuration(velocityY), childConsumed)
        }

        return true
    }

    /**
     * 根据速度计算滚动动画持续时间
     * @param velocityY
     * @return
     */
    private fun computeDuration(velocityY: Float): Int {
        var velocityY = velocityY
        val distance: Int
        distance = if (velocityY > 0) {
            Math.abs(mTop.height - scrollY)
        } else {
            Math.abs(mTop.height - (mTop.height - scrollY))
        }
        val duration: Int
        velocityY = Math.abs(velocityY)
        if (velocityY > 0) {
            duration = 3 * Math.round(1000 * (distance / velocityY))
        } else {
            val distanceRatio = distance.toFloat() / height
            duration = ((distanceRatio + 1) * 150).toInt()
        }

        return duration

    }

    /**
     *
     * @param velocityY
     * @param duration
     * @param childConsumed
     */
    private fun animateScroll(velocityY: Float, duration: Int, childConsumed: Boolean) {
        val currentOffset = scrollY
        val topHeight = mTop.height
        if (mOffsetAnimator == null) {
            mOffsetAnimator = ValueAnimator()
            mOffsetAnimator?.addUpdateListener { animation ->
                if (animation.animatedValue is Int) {
                    scrollTo(0, animation.animatedValue as Int)
                }
            }
        } else {
            mOffsetAnimator?.cancel()
        }
        mOffsetAnimator?.duration = Math.min(duration, 600).toLong()

        if (velocityY >= 0) {
            //向上滑动到topView不可见
            mOffsetAnimator?.setIntValues(currentOffset, topHeight)
            mOffsetAnimator?.start()
        } else {
            //如果子View没有消耗flying事件 那么就让自身滑倒0位置
            if (!childConsumed) {
                mOffsetAnimator?.setIntValues(currentOffset, 0)
                mOffsetAnimator?.start()
            }
        }
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
            scrollTo(0, mScroller.currY)
            invalidate()
        }
    }


}