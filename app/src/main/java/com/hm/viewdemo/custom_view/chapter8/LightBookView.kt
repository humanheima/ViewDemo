package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class LightBookView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 400
    }


    private var dstBmp: Bitmap
    private var srcBmp: Bitmap

    private var paint: Paint = Paint()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        dstBmp = BitmapFactory.decodeResource(resources, R.drawable.book_bg)
        srcBmp = BitmapFactory.decodeResource(resources, R.drawable.book_light)
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


    private val LIGHTEN = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)

    override fun onDraw(canvas: Canvas) {

        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = LIGHTEN
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)

        canvas.restoreToCount(layerId)
    }
}