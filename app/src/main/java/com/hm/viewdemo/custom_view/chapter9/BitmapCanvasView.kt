package com.hm.viewdemo.custom_view.chapter9

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by dumingwei on 2017/3/4.
 */
class BitmapCanvasView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "BitmapCanvasView"

    companion object {
        private val DEFAULT_SIZE = 200
    }

    private var mBmp: Bitmap
    private var mBmpCanvas: Canvas
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.BLACK
        mPaint.textSize = 40f
        mBmp = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        mBmpCanvas = Canvas(mBmp)
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
        //mBmpCanvas.drawText("欢迎光临", 0f, 100f, mPaint)
        //canvas.drawBitmap(mBmp, 0f, 0f, mPaint)
        canvas.translate(width / 2f, height / 2f)
        //canvas.drawText("欢迎光临", 0f, 0f, mPaint)
        val id = canvas.save()
        Log.d(TAG, "onDraw: id=$id")
        for (i in 0..5) {
            canvas.drawLine(0f, 0f, 100f, 0f, mPaint)
            canvas.rotate(60f)
        }
        canvas.restore()

    }

}
