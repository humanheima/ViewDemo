package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.hm.viewdemo.R
import kotlin.math.abs

/**
 * Created by dumingwei on 2021/7/31.
 *
 * Desc:自定义刻度尺控件
 */
class MyRulerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val TAG: String = "MyRulerView"

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    /**
     * 长、中、短刻度的高度
     */
    var maxLineHeight = 0
    var midLineHeight = 0
    var minLineHeight = 0

    /**
     * 线条宽度，默认12px
     */
    var lineWidth = 24

    var lineSpace = 96


    /**
     * 刻度尺的开始、结束数字
     */
    var mStartNum: Float = 0f
    var mEndNum: Float = 0f

    /**
     * 每个刻度代表的数字单位
     */
    var mUnitNum: Float = 1f

    private val mUnitRectF: RectF = RectF()

    //总共多少根线
    var mTotalLine: Int = 0

    /**
     * 控件绘制画笔
     */
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 文字画笔
     */
    private var textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 刻度文字大小
     */
    var textSize = 56f

    /**
     * 文字高度
     */
    var textHeight = 0f

    @ColorInt
    private var startColor = Color.parseColor("#ff3415b0")

    /**
     * 指示器半径
     */
    var indicatorRadius = lineWidth / 2

    private val indicatorPath = Path()

    private val mAlphaEnable: Boolean = false // 尺子 最左边 最后边是否需要透明 (透明效果更好点)

    //相对于屏幕中间的偏移量
    private var mOffset: Float = 0f

    //最小的偏移量，这是一个负数
    private var mMinOffset: Float = 0f

    //滑动的偏移量
    private var mMovedX: Float = 0f

    init {
        startColor = resources.getColor(R.color.colorPrimary)

        paint.color = startColor
        paint.style = Paint.Style.FILL
        paint.strokeCap = Paint.Cap.ROUND



        textPaint.color = startColor
        textPaint.style = Paint.Style.FILL
        textPaint.strokeCap = Paint.Cap.ROUND
        textPaint.textSize = textSize
        val fontMetrics = textPaint.fontMetrics
        textHeight = fontMetrics.descent - fontMetrics.ascent

        mTotalLine = ((mEndNum - mStartNum) / mUnitNum).toInt()

        Log.i(TAG, "init :mStartNum = $mStartNum , mStartNum = $mStartNum , mUnitNum = $mUnitNum , mTotalLine = $mTotalLine")

    }


    /**
     * @param startNum 开始值
     * @param endNum 结束值
     * @param unitNum 刻度值
     */
    fun setInitialValue(startNum: Float, endNum: Float, unitNum: Float) {
        mStartNum = startNum
        mEndNum = endNum
        mUnitNum = unitNum

        mTotalLine = ((mEndNum - mStartNum) / mUnitNum).toInt() + 1

        mOffset = if (mTotalLine % 2 == 0) {
            //偶数个刻度，需要偏移的量，中间两个刻度的中间在控件中间
            -(((mTotalLine - 1) * lineSpace + lineWidth) / 2f)
        } else {
            //奇数个刻度，需要偏移的量，保证中间的那个刻度矩形是在控件中间
            -((mTotalLine / 2) * lineSpace + lineWidth / 2f)

        }

        mMinOffset = -(mTotalLine - 1) * lineSpace - lineWidth / 2f

        Log.i(TAG, "init :mStartNum = $mStartNum , mStartNum = $mStartNum , mUnitNum = $mUnitNum , mTotalLine = $mTotalLine , mOffset = $mOffset")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        //最长刻度线的长度默认为控件总高度的2/3
        maxLineHeight = mHeight / 3
        midLineHeight = maxLineHeight * 4 / 5
        minLineHeight = maxLineHeight * 3 / 5

        indicatorPath.moveTo(mWidth / 2f - indicatorRadius, 0f)
        indicatorPath.lineTo(mWidth / 2f + indicatorRadius, 0f)
        indicatorPath.lineTo(mWidth / 2f, 2 * indicatorRadius.toFloat())
        indicatorPath.close()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mWidth <= 0 || mHeight <= 0) {
            return
        }
        canvas.drawColor(Color.CYAN)
        /**
         * 1. 先绘制指示器
         * 2. 绘制刻度
         * 3. 绘制文字
         *
         */
        canvas.drawPath(indicatorPath, paint)


        val startLeft = mWidth / 2f//以屏幕中间为基准

        val startTop = indicatorRadius * 4f

        var alphaPercent = 0f

        if (mTotalLine > 0) {
            for (i in 0 until mTotalLine) {

                var lineHeight = minLineHeight
                if (i % 10 == 0) {
                    lineHeight = maxLineHeight
                } else if (i % 5 == 0) {
                    lineHeight = midLineHeight
                }
                val left = startLeft + mOffset + lineSpace * i + mMovedX
                val right = left + lineWidth
                val bottom = startTop + lineHeight
                mUnitRectF.set(left, startTop, right, bottom)

                if (mAlphaEnable) {
                    alphaPercent = 1 - abs(left - startLeft) / startLeft
                    //为什么要乘以alphaPercent的平方呢？
                    val alpha = (255 * alphaPercent * alphaPercent).toInt()
                    paint.alpha = alpha
                    textPaint.alpha = alpha
                }
                val rx = lineWidth / 2f

                //绘制圆角矩形
                canvas.drawRoundRect(mUnitRectF, rx, rx, paint)

                val text = i.toString()
                canvas.drawText(text, left + lineWidth / 2 - textPaint.measureText(text) / 2, startTop + maxLineHeight + textHeight, textPaint)
            }
        }
    }

    /**
     * 用户手指按下控件滑动时的初始位置坐标
     */
    var mDownX = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = event.x

            }
            MotionEvent.ACTION_MOVE -> {
                mMovedX = event.x - mDownX
                //mOffset += mMovedX
                invalidate()

            }
            MotionEvent.ACTION_UP -> {
                mOffset += mMovedX
                //最大的偏移量
                val maxOffset = -lineWidth / 2f
                if (mOffset >= maxOffset) {
                    mOffset = maxOffset
                } else if (mOffset <= mMinOffset) {
                    //最小的偏移量
                    mOffset = mMinOffset
                } else {
                    //在合法的范围内，如果没有落到某个刻度中间，则调整某个
                }

                mMovedX = 0f

                Log.i(TAG, "onTouchEvent: mOffset = $mOffset")

                invalidate()
            }
        }
        return true
    }

}