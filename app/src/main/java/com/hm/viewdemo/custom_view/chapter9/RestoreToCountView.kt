package com.hm.viewdemo.custom_view.chapter9

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by dumingwei on 2019-09-04.
 * Desc:
 */
class RestoreToCountView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val TAG = "RestoreToCountView"

    companion object {
        private const val DEFAULT_SIZE = 400
    }

    private val mPaint: Paint = Paint()


    init {
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecModel = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecModel = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecModel == View.MeasureSpec.AT_MOST && heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE)
        } else if (widthSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize)
        } else if (heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_SIZE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw: count${canvas.saveCount}")
        val id1 = canvas.save()
        canvas.clipRect(0, 0, 800, 800)
        canvas.drawColor(Color.RED)
        Log.d(TAG, "onDraw: count${canvas.saveCount},id1=$id1")

        val id2 = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint, Canvas.ALL_SAVE_FLAG)
        canvas.clipRect(100, 100, 700, 700)
        canvas.drawColor(Color.GREEN)
        Log.d(TAG, "onDraw: count${canvas.saveCount},id2=$id2")

        val id3 = canvas.saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), 0xf0, Canvas.ALL_SAVE_FLAG)
        canvas.clipRect(200, 200, 600, 600)
        canvas.drawColor(Color.YELLOW)
        Log.d(TAG, "onDraw: count${canvas.saveCount},id3=$id3")

        val id4 = canvas.save()
        canvas.clipRect(300, 300, 500, 500)
        canvas.drawColor(Color.BLUE)
        Log.d(TAG, "onDraw: count${canvas.saveCount},id4=$id4")

        canvas.restoreToCount(id3)
        canvas.drawColor(Color.GRAY)
        Log.d(TAG, "onDraw: count${canvas.saveCount}")

    }
}