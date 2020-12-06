package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.hm.viewdemo.util.ScreenUtil

/**
 * Crete by dumingwei on 2019-08-14
 * Desc:
 */
class XFermodeViewAnother @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {


    companion object {


        private val TAG = "XFermodeView"

        private val HORIZONTAL_SPACE = 36
        private val VERTICAL_SPACE = 72
        private val ROW_MAX = 4   // number of samples per row
        private val DEFAULT_SIZE = 1000
        private val sModes = arrayOf<Xfermode>(
                PorterDuffXfermode(PorterDuff.Mode.CLEAR),
                PorterDuffXfermode(PorterDuff.Mode.SRC),
                PorterDuffXfermode(PorterDuff.Mode.DST),
                PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
                PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
                PorterDuffXfermode(PorterDuff.Mode.DST_IN),
                PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
                PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
                PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
                PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
                PorterDuffXfermode(PorterDuff.Mode.XOR),
                PorterDuffXfermode(PorterDuff.Mode.DARKEN),
                PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
                PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
                PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        )
        private val sLabels = arrayOf(
                "Clear",
                "Src",
                "Dst",
                "SrcOver",
                "DstOver",
                "SrcIn",
                "DstIn",
                "SrcOut",
                "DstOut",
                "SrcATop",
                "DstATop",
                "Xor",
                "Darken",
                "Lighten",
                "Multiply",
                "Screen")
    }

    private val mSrcB: Bitmap
    private val mDstB: Bitmap
    private val mBG: Shader     // background checker-board pattern

    private var itemWidth = 0
    private val labelP = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint = Paint()


    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        labelP.textAlign = Paint.Align.CENTER
        labelP.textSize = ScreenUtil.spToPx(getContext(), 14).toFloat()
        paint.isFilterBitmap = false

        itemWidth = (ScreenUtil.getScreenWidth(getContext()) - 5 * HORIZONTAL_SPACE) / ROW_MAX

        mSrcB = makeSrc(itemWidth, itemWidth)
        mDstB = makeDst(itemWidth, itemWidth)

        // make a ckeckerboard pattern 下面这几行代码可以注释掉，不影响
        val bm = Bitmap.createBitmap(intArrayOf(-0x1, -0x333334, -0x333334, -0x1), 2, 2,
                Bitmap.Config.RGB_565)
        mBG = BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        val m = Matrix()
        m.setScale(6f, 6f)
        mBG.setLocalMatrix(m)
    }

    // create a bitmap with a circle, used for the "dst" image
    private fun makeDst(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.setARGB(0xFF, 0xFF, 0xCC, 0x44)
        c.drawOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), p)
        return bm
    }

    // create a bitmap with a rect, used for the "src" image
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

        Log.i(TAG, "onMeasure: $widthSpecSize,$heightSpecSize")
        if (widthSpecModel == View.MeasureSpec.AT_MOST && heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE)
        } else if (widthSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize)
        } else if (heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_SIZE)
        }

    }

    override fun onDraw(canvas: Canvas) {
        Log.i(TAG, "onDraw: $measuredHeight,$measuredWidth")
        canvas.drawColor(Color.WHITE)
        canvas.translate(HORIZONTAL_SPACE.toFloat(), VERTICAL_SPACE.toFloat())
        var x = 0
        var y = 0
        for (i in sModes.indices) {
            // draw the border
            paint.style = Paint.Style.STROKE
            paint.shader = null
            canvas.drawRect(x - 0.5f, y - 0.5f, x.toFloat() + itemWidth.toFloat() + 0.5f, y.toFloat() + itemWidth.toFloat() + 0.5f, paint)

            // draw the checker-board pattern，这三行代码可以去掉，不影响
            paint.style = Paint.Style.FILL
            paint.shader = mBG
            canvas.drawRect(x.toFloat(), y.toFloat(), (x + itemWidth).toFloat(), (y + itemWidth).toFloat(), paint)

            // draw the src/dst example into our offscreen bitmap
            val sc = canvas.saveLayer(x.toFloat(), y.toFloat(), (x + itemWidth).toFloat(), (y + itemWidth).toFloat(), null, Canvas.ALL_SAVE_FLAG)

            canvas.translate(x.toFloat(), y.toFloat())
            canvas.drawBitmap(mDstB, 0f, 0f, paint)
            paint.xfermode = sModes[i]
            canvas.drawBitmap(mSrcB, (x + itemWidth) / 2f, (y + itemWidth) / 2f, paint)
            paint.xfermode = null
            canvas.restoreToCount(sc)

            // draw the label
            canvas.drawText(sLabels[i], x + itemWidth / 2f, y - labelP.textSize / 2, labelP)

            x += itemWidth + HORIZONTAL_SPACE
            // wrap around when we've drawn enough for one row
            if (i % ROW_MAX == ROW_MAX - 1) {
                x = 0
                y += itemWidth + VERTICAL_SPACE
            }
        }
    }


}
