package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by dumingwei on 2019-09-12.
 * Desc:
 */
class StrokeImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        val p = Paint()
        p.color = Color.CYAN
        setStateDrawable(this, p)
    }

    fun setStateDrawable(iv: StrokeImageView, p: Paint) {

        val bd = iv.drawable as BitmapDrawable?
        bd?.let {
            val srcBmp = it.bitmap
            srcBmp.isRecycled

            val bitmap = Bitmap.createBitmap(srcBmp.width, srcBmp.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(srcBmp.extractAlpha(), 0f, 0f, p)

            val sld = StateListDrawable()
            sld.addState(IntArray(android.R.attr.state_enabled), BitmapDrawable(srcBmp))
            sld.addState(IntArray(android.R.attr.state_pressed), BitmapDrawable(bitmap))

            //setBackgroundDrawable函数,会移除原有的padding值,如果需要padding,则需调用setPadding
            iv.setBackgroundDrawable(sld)
        }
    }
}