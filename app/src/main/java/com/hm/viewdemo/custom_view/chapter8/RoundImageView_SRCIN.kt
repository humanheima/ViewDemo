package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-01.
 * Desc:
 */
class RoundImageView_SRCIN @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint = Paint()

    private var bmpDST: Bitmap
    private var bmpSRC: Bitmap

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 45f

        bmpSRC = BitmapFactory.decodeResource(resources, R.drawable.ic_dog)
        bmpDST = BitmapFactory.decodeResource(resources, R.drawable.dog_shade)
    }

    private val rectF = RectF(0f, 0f, bmpDST.width.toFloat(), bmpDST.height.toFloat())

    private val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val tmpWidth = width / 2f
        val tmpHeight = tmpWidth * bmpDST.height / bmpDST.width
        canvas.save()
        rectF.set(0f, 0f, tmpWidth, tmpHeight)
        canvas.drawBitmap(bmpDST, null, rectF, paint)

        paint.xfermode = porterDuffXfermode

        canvas.drawBitmap(bmpSRC, null, rectF, paint)

        paint.xfermode = null

        canvas.restore()

    }

}