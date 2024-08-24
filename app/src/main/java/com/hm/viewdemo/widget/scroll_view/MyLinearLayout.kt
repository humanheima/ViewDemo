package com.hm.viewdemo.widget.scroll_view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout

/**
 * Created by dumingwei on 2020/11/29.
 *
 * Desc:
 */
class MyLinearLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val TAG: String = "MyLinearLayout"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.i(TAG, "onMeasure: measuredHeight = $measuredHeight")
    }

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        Log.i(TAG, "drawChild: ${canvas?.height} ${canvas?.hashCode()} $translationY")
        return super.drawChild(canvas, child, drawingTime)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.i(TAG, "onDraw: ${canvas?.height} ${canvas?.hashCode()}")
    }
}