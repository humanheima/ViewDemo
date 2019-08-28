package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class BitmapShaderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private var paint: Paint = Paint()

    private var rect = Rect()

    private var bitmap: Bitmap

    private var tileMode: Shader.TileMode = Shader.TileMode.REPEAT

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.BitmapShaderView)
            val style = ta.getInt(R.styleable.BitmapShaderView_tile_mode, 0)
            when (style) {
                0 -> tileMode = Shader.TileMode.REPEAT
                1 -> tileMode = Shader.TileMode.CLAMP
                2 -> tileMode = Shader.TileMode.MIRROR
            }
            ta.recycle()
        }
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.dog_edge)
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

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)

        /*val left = measuredWidth / 3f
        val top = measuredHeight / 3f
        val right = measuredWidth / 3 * 2f
        val bottom = measuredHeight / 3 * 2f
        canvas.drawRect(left, top, right, bottom, paint)*/

    }
}