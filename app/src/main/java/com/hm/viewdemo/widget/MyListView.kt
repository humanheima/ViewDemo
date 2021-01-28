package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ListView
import java.util.logging.Logger

/**
 * Created by dumingwei on 2021/1/24.
 *
 * Desc:
 */
class MyListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ListView(context, attrs, defStyleAttr) {


    private var mMotionY = 0
    private val TAG: String = "MyListView"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i(TAG, "onMeasure: childCount ${childCount} measuredWidth = $measuredWidth , measuredHeight =$measuredHeight")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.i(TAG, "onLayout: childCount ${childCount} measuredWidth = $measuredWidth , measuredHeight =$measuredHeight changed = $changed")

    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mMotionY = ev.y.toInt()
                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_DOWN $mMotionY")

            }
            MotionEvent.ACTION_MOVE -> {
                //Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_MOVE ${ev.y}")
                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_MOVE ${ev.y - mMotionY}")

            }

            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "onTouchEvent: MotionEvent.ACTION_UP")
                mMotionY = ev.y.toInt()
            }

        }
        return super.onTouchEvent(ev)
    }


}