package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-08-27.
 * Desc:
 */
class AvatarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 400
    }

    private var paint: Paint = Paint()

    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar_square)
    private var bitmapShader: BitmapShader
    private var tileMode: Shader.TileMode = Shader.TileMode.REPEAT

    private var mMatrix: Matrix = Matrix()

    init {
        bitmapShader = BitmapShader(bitmap, tileMode, tileMode)
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

    private val TAG = "AvatarView"

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLUE)
        Log.d(TAG, "onDraw: measuredWidth:$measuredWidth,measuredHeight:$measuredHeight")
        Log.d(TAG, "onDraw: $width,$height")
        val scaleX: Float = (width * 1.0f / bitmap.width)
        val scaleY: Float = (height * 1.0f / bitmap.height)

        Log.d(TAG, "onDraw: $scaleX,$scaleY")
        val scale = Math.min(scaleX, scaleY)
        mMatrix.setScale(scale, scale)
        bitmapShader.setLocalMatrix(mMatrix)
        paint.shader = bitmapShader
        val halfX = width / 2f
        val halfY = height / 2f
        val radius = Math.min(width / 2f, height / 2f)
        canvas.drawCircle(halfX, halfY, radius, paint)
    }
}