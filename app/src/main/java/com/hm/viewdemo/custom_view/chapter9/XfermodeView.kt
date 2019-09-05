package com.hm.viewdemo.custom_view.chapter9

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by dumingwei on 2019-08-27.
 * Desc: 为了方便固定大小为200dp
 */
class XfermodeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 400
    }

    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var dstBmp: Bitmap
    private var srcBmp: Bitmap

    private var xfermode: PorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    private var paint: Paint = Paint()

    init {
        bitmapWidth = ScreenUtil.dpToPx(context, 200)
        bitmapHeight = ScreenUtil.dpToPx(context, 200)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        dstBmp = makeDst(bitmapWidth, bitmapHeight)
        srcBmp = makeSrc(bitmapWidth, bitmapHeight)
    }


    /**
     * 注意我们的bitmap的尺寸就是整个控件的大小
     */
    private fun makeDst(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.setARGB(0xFF, 0xFF, 0xCC, 0x44)
        c.drawOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), p)
        return bm
    }

    /**
     * 注意我们的bitmap的尺寸就是整个控件的大小
     */
    private fun makeSrc(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.setARGB(0xFF, 0x66, 0xAA, 0xFF)
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), p)
        return bm
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
        drawSaveLayer(canvas)
        //drawNeverSaveLayer(canvas)
    }

    private fun drawNeverSaveLayer(canvas: Canvas) {
        canvas.drawColor(Color.GREEN)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(srcBmp, bitmapWidth / 2f, bitmapHeight / 2f, paint)
        paint.xfermode = null
    }

    private fun drawSaveLayer(canvas: Canvas) {
        canvas.drawColor(Color.GREEN)
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint, Canvas.ALL_SAVE_FLAG)

        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(srcBmp, bitmapWidth / 2f, bitmapHeight / 2f, paint)
        paint.xfermode = null
        canvas.restoreToCount(layerId)
    }
}