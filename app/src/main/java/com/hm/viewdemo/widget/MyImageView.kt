package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView

class MyImageView : ImageView {

    companion object {

        private val TAG = "MyImageView"
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "dispatchTouchEvent: " + event.action)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent: ${event.action}")
        return true
    }

}
