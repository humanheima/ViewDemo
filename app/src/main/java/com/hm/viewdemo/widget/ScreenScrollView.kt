package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-07-04.
 * Desc:一个跟手滑动的自定义View，拖动它可以在整个屏幕上随意滑动
 */
class ScreenScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "ScreenScrollView"

    private var mLastX = 0f
    private var mLastY = 0f
    private var paint: Paint = Paint()
    private var rectF: RectF

    init {
        paint.color = context.resources.getColor(R.color.colorPrimary)
        rectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRect(rectF, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "onTouchEvent: ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                Log.i(TAG, "onTouchEvent: ACTION_MOVE deltaX:$deltaX, deltaY:$deltaY")
                translationX += deltaX
                translationY += deltaY
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "onTouchEvent: ACTION_UP")
            }
            else -> {
                //do nothing

            }

        }
        mLastX = x
        mLastY = y

        return true
    }
}