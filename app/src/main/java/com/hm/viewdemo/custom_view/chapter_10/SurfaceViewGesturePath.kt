package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.concurrent.thread

/**
 * Created by dumingwei on 2019-09-15.
 * Desc:
 */
class SurfaceViewGesturePath @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private val TAG = "SurfaceViewGesturePath"

    private var paint = Paint()
    private var path = Path()

    init {

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.color = Color.RED

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent: ")
        val x = event.x
        val y = event.y


        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
        }
        drawCanvas()
        return super.onTouchEvent(event)
    }

    private fun drawCanvas() {
        //todo 这样直接创建线程，会创建很多线程吧，可以考虑使用线程池
        thread {
            val canvas = holder.lockCanvas()
            canvas.drawPath(path, paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

}