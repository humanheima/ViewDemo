package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-17
 * Desc:
 */

class CircleAvatarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val bitmap: Bitmap
    private val path: Path
    private val mPaint: Paint

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_soft_avatar)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        path = Path()

        val width = bitmap.width
        val height = bitmap.height

        path.addCircle(width / 2f, height / 2f, width / 2f, Path.Direction.CCW)
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

        canvas.drawColor(Color.RED)

        canvas.save()

        canvas.clipPath(path)

        canvas.drawBitmap(bitmap, 0f, 0f, mPaint)
        //canvas.drawColor(Color.GREEN);

        canvas.restore()

        //canvas.drawColor(Color.BLUE);

        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 3f
        mPaint.color = Color.GREEN
        mPaint.textSize = 54f
        mPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("great", bitmap.width / 2f, (bitmap.height + 50).toFloat(), mPaint)

    }

    companion object {
        private val DEFAULT_SIZE = 200
    }

}
