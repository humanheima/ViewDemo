package com.hm.viewdemo.custom_view.chapter9

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
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

    private val rectF = RectF()

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.set(0f, 0f, w.toFloat(), h.toFloat())
    }

//    override fun onDraw(canvas: Canvas) {
//        canvas.drawText("save前绘制", 40f, 100f, mPaint)
//        //canvas.drawBitmap(mBmp, 0f, 0f, mPaint)
//        //canvas.drawText("欢迎光临", 0f, 0f, mPaint)
//        val id = canvas.save()
//        //注释1处，这里应用了平移
//        canvas.translate(width / 2f, height / 2f)
//        mPaint.color = Color.RED
//        Log.i(TAG, "onDraw: id=$id")
//        for (i in 0..5) {
//            //坐标其实是从 0 + width/2f 开始
//            canvas.drawLine(0f, 0f, 100f, 0f, mPaint)
//            //旋转
//            canvas.rotate(60f)
//        }
//
//        canvas.restoreToCount(id)
//        //注释2处，save之后的平移操作，旋转 被取消。绘制还是在原来的坐标系
//        //画笔的颜色保留了，
//        canvas.drawText("restore后绘制", 40f, 240f, mPaint)
//
//
//    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText("save前绘制", 40f, 100f, mPaint)
        //canvas.drawBitmap(mBmp, 0f, 0f, mPaint)
        //canvas.drawText("欢迎光临", 0f, 0f, mPaint)
        //val id = canvas.saveLayerAlpha(rectF,64)
        val id = canvas.saveLayer(rectF, mPaint)
        //注释1处，这里应用了平移
        canvas.translate(width / 2f, height / 2f)
        mPaint.color = Color.RED
        Log.i(TAG, "onDraw: id=$id")
        for (i in 0..5) {
            //坐标其实是从 0 + width/2f 开始
            canvas.drawLine(0f, 0f, 100f, 0f, mPaint)
            //旋转
            canvas.rotate(60f)
        }

        canvas.restoreToCount(id)
        //注释2处，save之后的平移操作，旋转 被取消。绘制还是在原来的坐标系
        //画笔的颜色保留了，
        canvas.drawText("restore后绘制", 40f, 240f, mPaint)


    }


}
