package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Scroller
import com.hm.viewdemo.R
import java.util.*

/**
 * Created by dumingwei on 2020/12/28
 *
 *
 * Desc: 测试向上滚动的View
 */
class TestScrollerUpView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = javaClass.simpleName

    private var mScroller: Scroller

    /**
     * 绘制前面的文字的画笔
     */
    private var mPaint: Paint

    /**
     * 绘制"元"字的画笔
     */
    private var mRmbPaint: Paint

    private var mData: MutableList<String> = ArrayList()

    /**
     * 第一个Item的绘制Text的坐标
     */
    private var mFirstItemDrawX = 0
    private var mFirstItemDrawY = 0
    private var bgColor = 0
    private var mItemHeight = 0
    private var scrollDistance = 0
    private var textColor = 0
    private val rmb = "元"
    private var animatorDuration = 0

    fun setTextColor(@ColorRes textColor: Int) {
        this.textColor = textColor
    }

    init {
        mData.add("昔闻洞庭水")
        mData.add("今上岳阳楼")
        mData.add("念去去")
        mData.add("千里烟波")
        mData.add("暮霭沉沉楚天阔")
        mData.add("多情自古伤离别")
        mData.add("更哪堪冷落清秋节")
        animatorDuration = 3000
        bgColor = Color.LTGRAY
        textColor = resources.getColor(R.color.colorAccent)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = textColor
        mPaint.textSize = resources.getDimensionPixelSize(R.dimen.text_size_20dp).toFloat()
        mRmbPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRmbPaint.color = textColor
        mRmbPaint.textSize = resources.getDimensionPixelSize(R.dimen.text_size_14dp).toFloat()
        mScroller = Scroller(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mItemHeight = height
        val allHeight = mItemHeight * mData.size
        scrollDistance = allHeight - mItemHeight
        mFirstItemDrawX = width / 2
        //计算绘制第一个item时，画笔的Y坐标
        mFirstItemDrawY = ((mItemHeight - (mPaint.ascent() + mPaint.descent())) / 2).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        Log.i(TAG, "onDraw: mScroller.getFinalY()=" + mScroller.finalY)

        //绘制背景
        mPaint.color = bgColor
        val width = width
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
        mPaint.color = textColor

        //设置居中对齐
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.style = Paint.Style.FILL
        for (i in mData.indices) {
            val data = mData[i]
            val length = mPaint.measureText(data)
            Log.d(TAG, "onDraw:文字长度 $length")
            val itemDrawY = mFirstItemDrawY + i * mItemHeight
            canvas.drawText(data, mFirstItemDrawX.toFloat(), itemDrawY.toFloat(), mPaint)
            //绘制最后一个"元"字
            canvas.drawText(rmb, (width + length) / 2, itemDrawY.toFloat(), mRmbPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mItemHeight = measuredHeight
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val currY = mScroller.currY
            scrollTo(0, currY)
            invalidate()
        }
    }

    /**
     * 从0滚动到scrollDistance
     */
    fun smoothScrollUp() {
        val scrollY = scrollY
        if (scrollY != 0) {
            return
        }
        mScroller.startScroll(0, 0, 0, scrollDistance, animatorDuration)
        invalidate()
    }

    /**
     * 从scrollDistance滚动到0
     */
    fun smoothScrollDown() {
        val scrollY = scrollY
        if (scrollY != scrollDistance) {
            return
        }
        mScroller.startScroll(0, scrollY, 0, -scrollDistance, animatorDuration)
        invalidate()
    }

}