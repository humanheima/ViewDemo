package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class SweepGradientView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val TAG = "LinearGradientView"

    private var paint: Paint = Paint()
    private var radialGradient: SweepGradient? = null

    private var radius = 0
    private var tileMode: Shader.TileMode = Shader.TileMode.CLAMP

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.textSize = 80f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecModel = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecModel = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecModel == MeasureSpec.AT_MOST && heightSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE)
        } else if (widthSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize)
        } else if (heightSpecModel == MeasureSpec.AT_MOST) {
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
            radius = min(w, h) / 2
            radialGradient = SweepGradient(w / 2f, h / 2f, Color.RED, Color.GREEN)
            paint.shader = radialGradient
        }
    }

    /**
     * 多个颜色渐变
     */
    private fun manyColorGradient(w: Int, h: Int) {
        radius = min(w, h) / 2
        val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
        val positions = floatArrayOf(0f, 0.7f, 1f)

        radialGradient = SweepGradient(w / 2f, h / 2f, colors, positions)

        paint.shader = radialGradient
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, radius * 1.0f, paint)
    }

}