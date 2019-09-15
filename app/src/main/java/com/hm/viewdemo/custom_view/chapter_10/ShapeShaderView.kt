package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-10.
 * Desc:
 */
class ShapeShaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val RADIUS = 180
        const val FACTOR = 3
    }

    private var bitmap: Bitmap? = null

    private val bmp: Bitmap

    private var drawable: ShapeDrawable? = null

    private val mMatrix = Matrix()

    init {

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        bmp = BitmapFactory.decodeResource(resources, R.drawable.scenery)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        // 这个位置表示的是，画shader的起始位置
        mMatrix.setTranslate(RADIUS - x * FACTOR, RADIUS - y * FACTOR)

        drawable?.paint?.shader?.setLocalMatrix(mMatrix)

        // bounds，就是那个圆的外切矩形
        drawable?.setBounds((x - RADIUS).toInt(), (y - RADIUS).toInt(),
                (x + RADIUS).toInt(), (y + RADIUS).toInt())

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap == null) {
            //构建一个和view一样大小的bitmap
            bitmap = Bitmap.createScaledBitmap(bmp, width, height, false)

            //创建一个放大FACTOR倍的bitmap，然后使用该图片创建BitmapShader
            val shader = BitmapShader(Bitmap.createScaledBitmap(
                    bmp, width * FACTOR, height * FACTOR, true),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            drawable = ShapeDrawable(OvalShape())
            drawable?.paint?.shader = shader
            drawable?.setBounds(0, 0, RADIUS * 2, RADIUS * 2)
        }
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        drawable?.draw(canvas)
    }
}