package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class RadialGradientView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val TAG = "LinearGradientView"

    private var paint: Paint = Paint()
    private var radialGradient: RadialGradient? = null

    private var radius = 0
    private var tileMode: Shader.TileMode = Shader.TileMode.CLAMP

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.textSize = 80f
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            //twoColorGradient(w, h)
            manyColorGradient(w, h)
        }
    }

    /**
     * 两个颜色渐变
     */
    private fun twoColorGradient(w: Int, h: Int) {
        if (radialGradient == null) {
            radius = Math.min(w, h) / 2
            radialGradient = RadialGradient(w / 2f, h / 2f,
                    radius * 1.0f, Color.RED, Color.GREEN, tileMode)
            paint.shader = radialGradient
        }
    }

    /**
     * 多个颜色渐变
     */
    private fun manyColorGradient(w: Int, h: Int) {
        radius = Math.min(w, h) / 2
        val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
        val positions = floatArrayOf(0f, 0.7f, 1f)

        radialGradient = RadialGradient(w / 2f, h / 2f, radius * 1.0f,
                colors, positions, tileMode)

        paint.shader = radialGradient
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, radius * 1.0f, paint)
    }

}