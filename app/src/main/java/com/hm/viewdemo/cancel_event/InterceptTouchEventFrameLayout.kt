package com.hm.viewdemo.cancel_event

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * Created by dumingwei on 2020-03-02.
 * Desc:
 */
class InterceptTouchEventFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TAG = "InterceptTouchEventFram"


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i(TAG, "onInterceptTouchEvent: ${ev.action}")
        when (ev.action) {
            //MotionEvent.ACTION_MOVE -> return true
            MotionEvent.ACTION_UP -> return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onDraw(canvas: Canvas) {
        Log.i(TAG, "onDraw: canvas = ${canvas.hashCode()}")
        super.onDraw(canvas)

    }
}