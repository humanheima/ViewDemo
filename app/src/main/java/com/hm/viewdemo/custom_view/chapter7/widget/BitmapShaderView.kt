package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 * Shader.TileMode.CLAMP会将边缘的像素进行拉伸、扩展到整个View的宽度或高度
 * Shader.TileMode.MIRROR在绘制的矩形区域内，X轴方向和Y轴方向上出现了镜面翻转 直到占满整个View的宽高
 * Shader.TileMode.REPEAT 将图像进行复制平铺 跟电脑桌面壁纸一样 占不满一屏会进行平铺
 *
 * 参考链接：https://www.jianshu.com/p/83af13b41bb6
 *
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