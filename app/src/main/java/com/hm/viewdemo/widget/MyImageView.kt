package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView

class MyImageView : ImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "dispatchTouchEvent: " + event.action)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "onTouchEvent: ${event.action}")
        return true
    }

    companion object {

        private val TAG = "MyImageView"
    }

}
