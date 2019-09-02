package com.hm.viewdemo.custom_view.chapter8

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-01.
 * Desc:
 */
class TextWave_DSTIN @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mPaint: Paint

    private var mPath: Path

    private var mItemWaveLength = 1000

    private var dx = 0

    private var bmpSrc: Bitmap
    private var bmpDst: Bitmap

    init {

        mPath = Path()
        mPaint = Paint()
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.FILL_AND_STROKE

        bmpSrc = BitmapFactory.decodeResource(resources, R.drawable.text_shade, null)
        bmpDst = Bitmap.createBitmap(bmpSrc.width, bmpSrc.height, Bitmap.Config.ARGB_8888)

        startAnim()
    }

    private fun startAnim() {
        val animator = ValueAnimator.ofInt(0, mItemWaveLength)
        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            dx = animation.animatedValue as Int
            postInvalidate()
        }
        animator.start()
    }

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        generateWavePath()

        val c = Canvas(bmpDst)
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        c.drawPath(mPath, mPaint)

        //先绘制文字
        canvas.drawBitmap(bmpSrc, 0f, 0f, mPaint)

        val layerId = canvas.saveLayer(0f, 0f, width * 1.0f, height * 1.0f,
                null, Canvas.ALL_SAVE_FLAG)

        canvas.drawBitmap(bmpDst, 0f, 0f, mPaint)

        mPaint.xfermode = xfermode


        canvas.drawBitmap(bmpSrc, 0f, 0f, mPaint)
        mPaint.xfermode = null
        canvas.restoreToCount(layerId)

    }

    private fun generateWavePath() {

        mPath.reset()
        val originY = bmpSrc.height / 2
        val halfWaveLen = mItemWaveLength / 2

        mPath.moveTo((-mItemWaveLength + dx).toFloat(), originY.toFloat())
        var i = -mItemWaveLength
        while (i <= width + mItemWaveLength) {
            mPath.rQuadTo((halfWaveLen / 2).toFloat(), -50f, halfWaveLen.toFloat(), 0f)
            mPath.rQuadTo((halfWaveLen / 2).toFloat(), 50f, halfWaveLen.toFloat(), 0f)
            i += mItemWaveLength
        }

        mPath.lineTo(bmpSrc.width.toFloat(), bmpDst.height.toFloat())
        mPath.lineTo(0f, bmpSrc.height.toFloat())
        mPath.close()
    }
}