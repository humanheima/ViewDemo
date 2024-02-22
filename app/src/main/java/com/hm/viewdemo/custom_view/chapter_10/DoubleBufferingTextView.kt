package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.concurrent.thread

/**
 * Created by dumingwei on 2019-09-15.
 * Desc:
 */
class DoubleBufferingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var paint = Paint()

    init {
        paint.color = Color.RED
        paint.textSize = 30f
        holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                drawText(holder)
            }
        })
    }

    private fun drawText(holder: SurfaceHolder) {

        thread {
            for (i in 0..10) {

                val canvas = holder.lockCanvas()
                canvas?.drawText("$i", i * 30f, 50f, paint)
                holder.unlockCanvasAndPost(canvas)
                Thread.sleep(800)
            }
        }
    }
}