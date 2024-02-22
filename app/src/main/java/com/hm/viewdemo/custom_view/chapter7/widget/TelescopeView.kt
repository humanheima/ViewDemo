package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class TelescopeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private var paint: Paint = Paint()

    private var rect = Rect()

    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.scenery)

    /**
     * 用来充满整个屏幕
     */
    private var bitmapBG: Bitmap? = null

    private var tileMode: Shader.TileMode = Shader.TileMode.REPEAT

    private lateinit var bitmapShader: BitmapShader

    private var mDx = -1f
    private var mDy = -1f

    init {
        paint.shader = BitmapShader(bitmap, tileMode, tileMode)
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDx = event.x
                mDy = event.y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                mDx = event.x
                mDy = event.y
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mDx = -1f
                mDy = -1f
            }
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    private val TAG = "TelescopeView"

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i(TAG, "onSizeChanged: $w,$h")
        if (bitmapBG == null && w > 0 && h > 0) {
            bitmapBG = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvasBg = Canvas(bitmapBG!!)
            rect.set(0, 0, width, height)
            canvasBg.drawBitmap(bitmap, null, rect, paint)
        }
    }

    override fun onDraw(canvas: Canvas) {
        draw1(canvas)
    }

    private fun draw1(canvas: Canvas) {
        if (mDx > -1f && mDy > -1f) {
            paint.shader = BitmapShader(bitmapBG!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            canvas.drawCircle(mDx, mDy, 150f, paint)
        }
    }

    private fun draw2(canvas: Canvas) {
        if (bitmapBG == null) {
            bitmapBG = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvasBg = Canvas(bitmapBG!!)
            rect.set(0, 0, width, height)
            canvasBg.drawBitmap(bitmap, null, rect, paint)
        }
        if (mDx > -1f && mDy > -1f) {
            paint.shader = BitmapShader(bitmapBG!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            canvas.drawCircle(mDx, mDy, 150f, paint)
        }
    }
}