package com.hm.viewdemo.custom_view.chapter7.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.hm.viewdemo.dp2px
import com.hm.viewdemo.extension.dp2pxFloat

/**
 * Created by dumingwei on 2019-08-26.
 * Desc: https://github.com/lihangleo2/ShadowLayout 阴影View开源库，核心源码学习。
 */
class ShadowLayerFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ShadowLayerFrameLayout"

    }

    private var paint = Paint()

    private var mRadius = 1f
    private var mDx = 0f
    private var mDy = 0f

    private var rect = Rect()

    private var mSetShadow = true

    var mCornerRadius = 0f
    var mShadowLimit = 0
    var mShadowColor = 0

    var gradientDrawable: GradientDrawable = GradientDrawable()


    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mCornerRadius = 16.dp2pxFloat(context)
        mShadowLimit = 10.dp2px(context)
        mShadowColor = Color.GREEN

        Log.d(TAG, ": mCornerRadius $mCornerRadius mShadowLimit $mShadowLimit ")

        setPadding(mShadowLimit, mShadowLimit, mShadowLimit, mShadowLimit)

        gradientDrawable.setColor(Color.WHITE)
        gradientDrawable.setCornerRadius(mCornerRadius)
//        paint.color = Color.BLACK
//        paint.textSize = 25f
//        dogBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_dog)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h)
            rect.set(mShadowLimit, mShadowLimit, w - mShadowLimit, h - mShadowLimit)
            Log.d(TAG, "onSizeChanged: rect = $rect")
        }

    }

    private fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap: Bitmap = createShadowBitmap(
            w,
            h,
            mCornerRadius,
            mShadowLimit.toFloat(),
            mDx,
            mDy,
            mShadowColor,
            Color.TRANSPARENT
        )
        val drawable = BitmapDrawable(bitmap)
        background = drawable
    }

    private fun createShadowBitmap(
        shadowWidth: Int,
        shadowHeight: Int,
        cornerRadius: Float,
        shadowRadius: Float,
        dx: Float,
        dy: Float,
        shadowColor: Int,
        fillColor: Int
    ): Bitmap {
        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(output)

        //这里缩小limit的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        //同时也要根据某边的隐藏情况去改变
        val rect_left = shadowRadius
        val rect_right = shadowWidth - shadowRadius
        val rect_top = shadowRadius
        val rect_bottom = shadowHeight - shadowRadius

        val shadowRect = RectF(
            rect_left,
            rect_top,
            rect_right,
            rect_bottom
        )


        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        paint.color = fillColor
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, paint)
        return output

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        gradientDrawable.bounds = rect
        gradientDrawable.draw(canvas)
    }

    fun addRadius() {
        mRadius++
        postInvalidate()
    }

    fun subtractRadius() {
        mRadius--
        postInvalidate()
    }

    fun subtractDx() {
        mDx--
        postInvalidate()
    }

    fun subtractDy() {
        mDy--
        postInvalidate()
    }

    fun addDx() {
        mDx++
        postInvalidate()
    }

    fun addDy() {
        mDy++
        postInvalidate()
    }

    fun setShadow(showShadow: Boolean) {
        mSetShadow = showShadow
        postInvalidate()
    }

}