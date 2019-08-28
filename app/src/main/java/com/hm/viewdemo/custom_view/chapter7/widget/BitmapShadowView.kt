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
class BitmapShadowView @JvmOverloads constructor(
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

    /**
     * 只有原图片alpha值的bitmap
     */
    private var alphaBitmap: Bitmap

    private var blurMaskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat_dog)
        alphaBitmap = bitmap.extractAlpha()

        paint.color = Color.BLACK
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
        val width = measuredWidth / 2
        val height = width * alphaBitmap.height / alphaBitmap.width

        rect.set(10, 10, width, height)

        //绘制阴影
        paint.maskFilter = blurMaskFilter
        canvas.drawBitmap(alphaBitmap, null, rect, paint)

        //绘制原图
        canvas.translate(-5f, -5f)
        rect.set(0, 0, width, height)
        canvas.drawBitmap(bitmap, null, rect, paint)
    }
}