package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-26.
 * Desc:
 */
class ShadowLayerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private var paint = Paint()
    private var dogBitmap: Bitmap

    private var mRadius = 1f
    private var mDx = 10f
    private var mDy = 10f

    private var rect = Rect()

    private var mSetShadow = true

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.color = Color.BLACK
        paint.textSize = 25f
        dogBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_dog)
    }

    fun addRadius() {
        mRadius++
        postInvalidate()
    }

    fun subtractRadius() {
        mRadius--
        postInvalidate()
    }

    fun subtractDx() {
        mDx--
        postInvalidate()
    }

    fun subtractDy() {
        mDy--
        postInvalidate()
    }

    fun addDx() {
        mDx++
        postInvalidate()
    }

    fun addDy() {
        mDy++
        postInvalidate()
    }

    fun setShadow(showShadow: Boolean) {
        mSetShadow = showShadow
        postInvalidate()
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
        canvas.drawColor(Color.WHITE)
        if (mSetShadow) {
            paint.setShadowLayer(mRadius, mDx, mDy, Color.GRAY)

        } else {
            paint.clearShadowLayer()
        }
        canvas.drawText("杜明伟", 100f, 100f, paint)
        canvas.drawCircle(300f, 100f, 50f, paint)
        rect.set(500, 50, 500 + dogBitmap.width, 50 + dogBitmap.height)
        canvas.drawBitmap(dogBitmap, null,
                rect, paint)
    }


}