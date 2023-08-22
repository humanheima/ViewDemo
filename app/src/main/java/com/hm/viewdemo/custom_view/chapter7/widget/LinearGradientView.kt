package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class LinearGradientView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val TAG = "LinearGradientView"

    private var paint: Paint = Paint()
    private var mMatrix = Matrix()
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
            manyColorGradient(w, h)
        }
    }


    /**
     * 多个颜色渐变
     */
    private fun manyColorGradient(w: Int, h: Int) {

        val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
        val positions = floatArrayOf(0f, 0.7f, 1f)

        val linearGradient = LinearGradient(
            0f, 0f, w * 1.0f, 0f,
            colors, positions, tileMode
        )

        paint.shader = linearGradient
    }

    override fun onDraw(canvas: Canvas) {
        drawGradientText(canvas)

    }

    private var tran = 0
    private var advance = 5
    private var text = "Android绘图小糊涂,Android绘图小糊涂,Android绘图小糊涂,Android绘图小糊涂,Android绘图小糊涂,Android绘图小糊涂"

    private var rect = Rect()

    //绘制渐变文字
    private fun drawGradientText(canvas: Canvas) {
        val colors = intArrayOf(Color.BLACK, Color.RED)
        //val colors = intArrayOf(0xff000000.toInt(), 0x7f000000, 0x0f000000)
        paint.getTextBounds(text, 0, text.length, rect)

        val textWidth = rect.width()

        Log.i(TAG, "drawGradientText: $textWidth")
        val linearGradient = LinearGradient(
            0f, 0f, -width * 1.0f, 0f,
            colors, null, Shader.TileMode.CLAMP
        )

        mMatrix.setTranslate(tran.toFloat(), 0f)
        linearGradient.setLocalMatrix(mMatrix)

        tran += advance

        if (tran >= textWidth * 2) {
            tran = 0
        }

        paint.shader = linearGradient
        canvas.drawText(text, 0f, height / 2f, paint)
        invalidate()
    }

}