package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
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

    private var selectedPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

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

    private var mSelectedColor = Color.RED //文字的颜色


    private var indicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    //指示器半径
    var indicatorRadius = lineWidth / 2

    private val indicatorPath = Path()

    private val mAlphaEnable: Boolean = true // 尺子 最左边 最后边是否需要透明 (透明效果更好点)

    //相对于屏幕中间的偏移量
    private var mOffset: Float = 0f

    private var mStartOffset: Float = 0f

    //最小的偏移量，这是一个负数
    private var mMinOffset: Float = 0f

    //滑动的偏移量
    private var mMovedX: Float = 0f

    private var mMinimumVelocity = 0f
    private var mMaximumVelocity = 0f
    private var mVelocityTracker: VelocityTracker = VelocityTracker.obtain()

    /**
     * 辅助计算滑动，主要用于惯性计算
     */
    private var scroller: Scroller

    private var mSelectedNum: Float = 0f

    var onNumSelectListener: OnNumSelectListener? = null

    init {

        val configuration = ViewConfiguration.get(context)
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity.toFloat()
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity.toFloat()

        scroller = Scroller(getContext())

        startColor = resources.getColor(R.color.colorPrimary)

        indicatorPaint.color = startColor
        indicatorPaint.style = Paint.Style.FILL
        indicatorPaint.strokeCap = Paint.Cap.ROUND

        paint.color = startColor
        paint.style = Paint.Style.FILL
        paint.strokeCap = Paint.Cap.ROUND

        selectedPaint.color = mSelectedColor
        selectedPaint.style = Paint.Style.FILL
        selectedPaint.strokeCap = Paint.Cap.ROUND

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
     * @param selectedNUm 初始选中的值
     * @param unitNum 刻度值
     */
    fun setInitialValue(startNum: Float, endNum: Float, selectedNUm: Float, unitNum: Float) {
        mStartNum = startNum
        mEndNum = endNum
        mSelectedNum = selectedNUm

        mUnitNum = unitNum

        mTotalLine = ((mEndNum - mStartNum) / mUnitNum).toInt() + 1

//        mOffset = if (mTotalLine % 2 == 0) {
//            //偶数个刻度，需要偏移的量，中间两个刻度的中间在控件中间
//            -(((mTotalLine - 1) * lineSpace + lineWidth) / 2f)
//        } else {
//            //奇数个刻度，需要偏移的量，保证中间的那个刻度矩形是在控件中间
//            -((mTotalLine / 2) * lineSpace + lineWidth / 2f)
//        }

        mOffset = -((mSelectedNum - mStartNum) * lineSpace + lineWidth / 2f)

        //mOffset = 0f

        mStartOffset = mOffset

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
        canvas.drawPath(indicatorPath, indicatorPaint)

        val startLeft = mWidth / 2f//以屏幕中间为基准
        val startTop = indicatorRadius * 4f

        var alphaPercent = 0f

        if (mTotalLine > 0) {
            for (i in 0 until mTotalLine) {

                var lineHeight = minLineHeight
                if (i == 0 || (i + 1) % 10 == 0) {
                    lineHeight = maxLineHeight
                } else if ((i + 1) % 5 == 0) {
                    lineHeight = midLineHeight
                }
                val left = startLeft + mOffset + lineSpace * i + mMovedX
                val right = left + lineWidth
                val bottom = startTop + lineHeight
                mUnitRectF.set(left, startTop, right, bottom)

                val rx = lineWidth / 2f

                val center = (left + right) / 2f


                /**
                 * 距离控件水平方向上正中间的距离
                 */
                val absToCenter = abs(startLeft - center)
                if (absToCenter < lineSpace) {
                    var selectedAlpha = 127 + (128 * (1 - absToCenter * 1.0f / lineSpace)).toInt()
                    Log.i(TAG, "onDraw: alpha = $selectedAlpha")
                    if (selectedAlpha >= 255) {
                        selectedAlpha = 255
                    }
                    if (selectedAlpha <= 127) {
                        selectedAlpha = 127
                    }
                    selectedPaint.alpha = selectedAlpha
                    //使用选中的颜色的画笔
                    canvas.drawRoundRect(mUnitRectF, rx, rx, selectedPaint)
                } else {
                    if (mAlphaEnable) {
                        alphaPercent = 1 - abs(center - startLeft) / startLeft
                        //为什么要乘以alphaPercent的平方呢？
                        //val alpha = (255 * alphaPercent * alphaPercent).toInt()
                        val alpha = (255 * alphaPercent).toInt()
                        paint.alpha = alpha
                        textPaint.alpha = alpha
                    }
                    canvas.drawRoundRect(mUnitRectF, rx, rx, paint)
                }

                val text = (i + 1).toString()
                canvas.drawText(text, left + lineWidth / 2 - textPaint.measureText(text) / 2, startTop + maxLineHeight + textHeight, textPaint)
            }
        }
    }

    /**
     * 用户手指按下控件滑动时的初始位置坐标
     */
    var mDownX = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mMovedX = 0f
                mDownX = event.x

            }
            MotionEvent.ACTION_MOVE -> {
                mMovedX = event.x - mDownX
                val selectedNum = getSelectedNum()
                Log.i(TAG, "onTouchEvent: ACTION_MOVE selectedNum =$selectedNum")
                onNumSelectListener?.onNumSelect(selectedNum)
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
                    if (mOffset < mStartOffset) {
                        //手指从右向左滑动，diff小于0，
                        val diff = (mOffset - mStartOffset) % lineSpace
                        //如果滑动距离超过lineSpace的一半，继续向左移动，落到下一个刻度中间
                        if (abs(diff) >= lineSpace / 2) {
                            mOffset -= (lineSpace + diff)
                        } else {
                            //向右移动，落到刚才的刻度上
                            mOffset -= diff
                        }
                    } else {
                        //手指从左向右滑动
                        //手指从右向左滑动，diff大于0，
                        val diff = (mOffset - mStartOffset) % lineSpace
                        //如果滑动距离超过lineSpace的一半，继续向左移动，落到下一个刻度中间
                        if (diff >= lineSpace / 2) {
                            mOffset += (lineSpace - diff)
                        } else {
                            //向右移动，落到刚才的刻度上
                            mOffset -= diff
                        }
                    }
                }

                mMovedX = 0f

                val selectedNum = getSelectedNum()
                Log.i(TAG, "onTouchEvent: ACTION_UP selectedNum =$selectedNum")
                onNumSelectListener?.onNumSelect(selectedNum)

                mVelocityTracker.computeCurrentVelocity(500, mMaximumVelocity)

                val velocityX = mVelocityTracker.xVelocity
                Log.i(TAG, "onTouchEvent: velocityX = $velocityX mMinimumVelocity = $mMinimumVelocity")

                if (abs(velocityX) > mMinimumVelocity) {
                    scroller.fling(0, 0, velocityX.toInt(), 0, Int.MIN_VALUE, Int.MAX_VALUE, 0, 0)
                }

                Log.i(TAG, "onTouchEvent: mOffset = $mOffset")

                invalidate()
            }
        }
        return true
    }


    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (scroller.currX == scroller.finalX) {
                mMovedX = scroller.currX - scroller.startX.toFloat()
                Log.i(TAG, "computeScroll: scroller.currX == scroller.finalX")
                //最大的偏移量
                val maxOffset = -lineWidth / 2f
                if (mOffset + mMovedX >= maxOffset) {
                    mOffset = maxOffset
                    mMovedX = 0f
                    scroller.forceFinished(true)
                } else if (mOffset + mMovedX <= mMinOffset) {
                    //最小的偏移量
                    mOffset = mMinOffset
                    mMovedX = 0f
                    scroller.forceFinished(true)
                } else {
                    mOffset += mMovedX
                    if (mOffset < mStartOffset) {
                        //手指从右向左滑动，diff小于0，
                        val diff = (mOffset - mStartOffset) % lineSpace
                        //如果滑动距离超过lineSpace的一半，继续向左移动，落到下一个刻度中间
                        if (abs(diff) >= lineSpace / 2) {
                            mOffset -= (lineSpace + diff)
                        } else {
                            //向右移动，落到刚才的刻度上
                            mOffset -= diff
                        }
                    } else {
                        //手指从左向右滑动
                        //手指从右向左滑动，diff大于0，
                        val diff = (mOffset - mStartOffset) % lineSpace
                        //如果滑动距离超过lineSpace的一半，继续向左移动，落到下一个刻度中间
                        if (diff >= lineSpace / 2) {
                            mOffset += (lineSpace - diff)
                        } else {
                            //向右移动，落到刚才的刻度上
                            mOffset -= diff
                        }
                    }

                    mMovedX = 0f
                }

            } else {
                mMovedX = scroller.currX - scroller.startX.toFloat()
                //最大的偏移量
                val maxOffset = -lineWidth / 2f
                if (mOffset + mMovedX >= maxOffset) {
                    mOffset = maxOffset
                    mMovedX = 0f
                    scroller.forceFinished(true)
                } else if (mOffset + mMovedX <= mMinOffset) {
                    //最小的偏移量
                    mOffset = mMinOffset
                    mMovedX = 0f
                    scroller.forceFinished(true)
                }
            }
            val selectedNum = getSelectedNum()
            Log.i(TAG, "computeScroll: selectedNum =$selectedNum")
            onNumSelectListener?.onNumSelect(selectedNum)
            postInvalidate()
        }
    }

    fun getSelectedNum(): Float {
        return (abs(mOffset) - lineWidth / 2) / lineSpace + 1
    }


    interface OnNumSelectListener {
        fun onNumSelect(selectedNum: Float)
    }
}