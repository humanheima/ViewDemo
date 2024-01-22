package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class BlurMaskFilterView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private var paint: Paint = Paint()

    private var blurStyle = BlurMaskFilter.Blur.NORMAL

    init {

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.BlurMaskFilterView)
            val style = ta.getInt(R.styleable.BlurMaskFilterView_blur_style, 0)
            when (style) {
                0 -> blurStyle = BlurMaskFilter.Blur.NORMAL
                1 -> blurStyle = BlurMaskFilter.Blur.SOLID
                2 -> blurStyle = BlurMaskFilter.Blur.OUTER
                3 -> blurStyle = BlurMaskFilter.Blur.INNER
            }
            ta.recycle()
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.color = Color.BLUE
        paint.maskFilter = BlurMaskFilter(50f, blurStyle)
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
        canvas.drawCircle(measuredWidth / 2f, measuredWidth / 2f,
                Math.min(measuredWidth, measuredHeight) / 3f, paint)
    }
}