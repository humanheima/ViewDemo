package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class ComposeShaderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 200
    }

    private val TAG: String = "ComposeShaderView"

    private var paint: Paint = Paint()
    private var linearGradient: LinearGradient

    private var bitmapShader: BitmapShader

    private var composeShader: ComposeShader
    private var tileMode: Shader.TileMode = Shader.TileMode.CLAMP
    private var bitmap: Bitmap

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        paint.textSize = 80f
        bitmap = (resources.getDrawable(R.drawable.icon) as BitmapDrawable).bitmap
        bitmapShader = BitmapShader(bitmap, tileMode, tileMode)

        linearGradient = LinearGradient(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(),
                Color.RED, Color.BLUE, tileMode)

        composeShader = ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY)

        paint.shader = composeShader

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
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
    }

}