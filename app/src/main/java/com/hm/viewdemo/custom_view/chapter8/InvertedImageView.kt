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
class InvertedImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 400
    }


    private var dstBmp: Bitmap
    private var srcBmp: Bitmap

    private var bmpRevert: Bitmap

    private var paint: Paint = Paint()
    private var rect = Rect()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        dstBmp = BitmapFactory.decodeResource(resources, R.drawable.dog_invert_shade)
        srcBmp = BitmapFactory.decodeResource(resources, R.drawable.ic_dog)

        val mMatrix = Matrix()
        mMatrix.setScale(1f, -1f)
        bmpRevert = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.width, srcBmp.height, mMatrix, true)
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


    private val SRC_IN = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onDraw(canvas: Canvas) {

        val tmpWidth = width / 2

        val tmpHeight = tmpWidth * dstBmp.height / dstBmp.width

        rect.set(0, 0, tmpWidth, tmpHeight)
        canvas.drawBitmap(srcBmp, null, rect, paint)

        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        canvas.translate(0f, tmpHeight.toFloat())

        rect.set(0, 0, tmpWidth, tmpHeight)

        canvas.drawBitmap(dstBmp, null, rect, paint)

        paint.xfermode = SRC_IN

        rect.set(0, 0, tmpWidth, tmpHeight)

        canvas.drawBitmap(bmpRevert, null, rect, paint)

        paint.xfermode = null

        canvas.restoreToCount(layerId)
    }
}