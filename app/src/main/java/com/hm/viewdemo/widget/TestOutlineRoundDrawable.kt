package com.hm.viewdemo.widget

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log

/**
 * Created by dumingwei on 2021/8/9
 *
 * Desc:
 */
class TestOutlineRoundDrawable : Drawable() {

    private val TAG: String = "TestOutlineRoundDrawabl"

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mBoundsF: RectF = RectF()
    private var mBoundsI: Rect = Rect()

    private var radius = 16f

    init {
        paint.color=Color.CYAN
    }

    override fun onBoundsChange(bounds: Rect?) {
        bounds?.let {
            mBoundsF.set(it)
            mBoundsI.set(it)
        }
        Log.i(TAG, "onBoundsChange: ${bounds?.toString()}")
    }

    /**
     * 覆盖了Drawable的getOutline方法
     */
    override fun getOutline(outline: Outline) {
        Log.i(TAG, "getOutline:")
        //注释1处，只有大于等于Build.VERSION_CODES.LOLLIPOP(21)版本以上才能调用Outline的setRoundRect方法。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outline.setRoundRect(mBoundsI, radius)
        } else {
            super.getOutline(outline)
        }
    }

    override fun draw(canvas: Canvas) {
        Log.i(TAG, "draw: $mBoundsF")
        canvas.drawRoundRect(mBoundsF, radius,radius, paint)
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

}