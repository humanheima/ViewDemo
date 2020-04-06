package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-17
 * Desc: 好像不行
 */

class ClipRegionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bitmap: Bitmap
    private var mRgn: Region

    private var paint = Paint()

    private var clipWidth = 0
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var rect = Rect()

    companion object {

        const val DEFAULT_SIZE = 200
        const val CLIP_HEIGHT = 30
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.scenery)

        bitmapWidth = bitmap.width
        bitmapHeight = bitmap.height
        mRgn = Region()

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

        mRgn.setEmpty()
        rect.setEmpty()
        var i = 0
        while (i * CLIP_HEIGHT <= height) {
            if (i % 2 == 0) {
                rect.set(0, i * CLIP_HEIGHT, clipWidth, (i + 1) * CLIP_HEIGHT)
                mRgn.union(rect)
            } else {
                rect.set(width - clipWidth, i * CLIP_HEIGHT, width, (i + 1) * CLIP_HEIGHT)
                mRgn.union(rect)
            }
            i++
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        if (clipWidth >= width) {
            return
        }

        clipWidth += 5
        invalidate()
    }


}
