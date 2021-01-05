package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
 * Desc: 测试向上向下滚动的View
 */
class TestScrollerUpDownView @JvmOverloads constructor(
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
     * 绘制最后一个字的画笔
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

    var textColor = 0
    var textSize = 0

    var lastWordTextColor = 0
    var lastWordTextSize = 0


    /**
     * 最后一个字
     */
    var lastWord: String = "元"
    var animatorDuration = 0

    init {
        val ta = context?.obtainStyledAttributes(attrs, R.styleable.TestScrollerUpDownView)
        ta?.let {
            textColor = it.getColor(R.styleable.TestScrollerUpDownView_text_color, Color.BLACK)
            textSize = it.getDimensionPixelSize(R.styleable.TestScrollerUpDownView_text_size, resources.getDimensionPixelSize(R.dimen.text_size_20dp))

            lastWord = it.getString(R.styleable.TestScrollerUpDownView_last_word) ?: ""
            lastWordTextColor = it.getColor(R.styleable.TestScrollerUpDownView_last_word_text_color, Color.BLACK)
            lastWordTextSize = it.getDimensionPixelSize(R.styleable.TestScrollerUpDownView_last_word_text_size, resources.getDimensionPixelSize(R.dimen.text_size_20dp))

            animatorDuration = it.getInt(R.styleable.TestScrollerUpDownView_animation_duration, 3000)
        }
        ta?.recycle()

        bgColor = Color.LTGRAY

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = textColor
        mPaint.textSize = textSize.toFloat()

        mRmbPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRmbPaint.color = lastWordTextColor
        mRmbPaint.textSize = lastWordTextSize.toFloat()

        mScroller = Scroller(context)
    }

    fun setData(data: MutableList<String>) {
        mData.clear()
        mData.addAll(data)
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
            canvas.drawText(lastWord, (width + length) / 2, itemDrawY.toFloat(), mRmbPaint)
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