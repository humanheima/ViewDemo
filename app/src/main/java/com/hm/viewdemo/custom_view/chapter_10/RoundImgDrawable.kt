package com.hm.viewdemo.custom_view.chapter_10

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by dumingwei on 2019-09-10.
 * Desc:
 */
class RoundImgDrawable(var bitmap: Bitmap) : Drawable() {

    private var paint: Paint = Paint()

    private var bitmapShader: BitmapShader? = null
    private var mBound: RectF? = null


    override fun draw(canvas: Canvas) {
        mBound?.let {
            canvas.drawRoundRect(it, 20f, 20f, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {

        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        bitmapShader = BitmapShader(Bitmap.createScaledBitmap(
                bitmap, right - left, bottom - top, true),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        paint.shader = bitmapShader

        mBound = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    override fun getIntrinsicHeight(): Int {
        return bitmap.height
    }

    override fun getIntrinsicWidth(): Int {
        return bitmap.width
    }
}