package com.hm.viewdemo.custom_view.chapter_11

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-15.
 * Desc:
 */
class Myview @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var rect = Rect()
    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_dog)

    /**
     * 增加亮度
     */
    private val colorMatrix0 = ColorMatrix(floatArrayOf(
            1.2f, 0f, 0f, 0f, 0f,
            0f, 1.2f, 0f, 0f, 0f,
            0f, 0f, 1.2f, 0f, 0f,
            0f, 0f, 0f, 1.2f, 0f
    ))

    /**
     * 红色通道
     */
    private val colorMatrix = ColorMatrix(floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
    ))

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rect.set(0, 0, 500, 500 * bitmap.height / bitmap.width)
        canvas.drawBitmap(bitmap, null, rect, paint)

        canvas.translate(510f, 0f)

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        rect.set(0, 0, 500, 500 * bitmap.height / bitmap.width)
        canvas.drawBitmap(bitmap, null, rect, paint)
    }

}