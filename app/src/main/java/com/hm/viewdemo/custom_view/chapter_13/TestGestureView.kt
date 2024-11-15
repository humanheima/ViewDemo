package com.hm.viewdemo.custom_view.chapter_13

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/**
 * Created by dumingwei on 2019-09-15.
 * Desc:测试各种手势检测
 *
 */
class TestGestureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        private val TAG = "TestGestureView"

        private const val DEFAULT_SIZE = 200

        private const val FLING_MIN_DISTANCE = 100
        private const val FLING_MIN_VELOCITY = 200

    }

    private var gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onShowPress(e: MotionEvent) {
                Log.i(TAG, "onShowPress: ${getActionName(e.action)}")
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.i(TAG, "onSingleTapUp: ${getActionName(e.action)}")
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                Log.i(TAG, "onDown: ${getActionName(e.action)}")
                return true
            }

            /**
             * 向右滑动velocityX是为正值
             * 向左滑动velocityX是为负值
             */
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 == null) return false
                Log.i(TAG, "onFling: ${getActionName(e1.action)},${getActionName(e2.action)}")
                Log.e(
                    TAG,
                    "onFling: e1.x=${e1.x}, e2.x=${e2.x}, velocityX=$velocityX, velocityY=$velocityY"
                )
                if (e1.x - e2.x > FLING_MIN_DISTANCE && abs(velocityX) >= FLING_MIN_VELOCITY) {
                    //向左滑
                    Log.e(TAG, "onFling: left")
                } else if (e2.x - e1.x > FLING_MIN_DISTANCE && abs(velocityX) >= FLING_MIN_VELOCITY) {
                    Log.e(TAG, "onFling: right")
                }
                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {

                Log.i(TAG, "onScroll: ${getActionName(e1?.action)},${getActionName(e2.action)}")
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                Log.i(TAG, "onLongPress: ${getActionName(e.action)}")
            }
        })

    init {
        gestureDetector.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                Log.i(TAG, "onDoubleTap: ${getActionName(e.action)}")
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                Log.i(TAG, "onDoubleTapEvent: ${getActionName(e.action)}")
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                Log.i(TAG, "onSingleTapConfirmed: ${getActionName(e.action)}")
                return true
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecModel = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecModel = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecModel == MeasureSpec.AT_MOST && heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE)
        } else if (widthSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize)
        } else if (heightSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_SIZE)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.CYAN)
    }

    private fun getActionName(action: Int?): String {
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                return "ACTION_DOWN"
            }

            MotionEvent.ACTION_MOVE -> {
                return "ACTION_MOVE"
            }

            MotionEvent.ACTION_UP -> {
                return "ACTION_UP"
            }

            MotionEvent.ACTION_CANCEL -> {
                return "ACTION_CANCEL"
            }

            else -> {
                return "UNKNOWN"
            }
        }
    }

}