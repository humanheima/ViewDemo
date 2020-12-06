package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.concurrent.thread

/**
 * Created by dumingwei on 2019-09-15.
 * Desc:
 */
class RectRefreshSurfaceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private val TAG = "RectRefreshSurfaceView"

    private var paint: Paint = Paint()

    init {

        paint.color = Color.argb(0x1F, 0xFF, 0xFF, 0xFF)
        paint.textSize = 30f

        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                drawText(holder)
            }
        })
    }

    private fun drawText(holder: SurfaceHolder) {
        thread {
            while (true) {
                val dirtyRect = Rect(0, 0, 1, 1)
                val canvas = holder.lockCanvas(dirtyRect)
                val canvasRect = canvas.clipBounds
                if (width == canvasRect.width() && height == canvasRect.height()) {
                    canvas.drawColor(Color.BLACK)
                    holder.unlockCanvasAndPost(canvas)
                } else {
                    holder.unlockCanvasAndPost(canvas)
                    break
                }
            }
            for (i in 0..10) {
                //画大方
                if (i == 0) {
                    val canvas = holder.lockCanvas(Rect(10, 10, 600, 600))
                    dumpCanvasRect(canvas)
                    canvas.drawColor(Color.RED)
                    holder.unlockCanvasAndPost(canvas)
                }

                if (i == 1) {
                    val canvas = holder.lockCanvas(Rect(30, 30, 570, 570))
                    dumpCanvasRect(canvas)
                    canvas.drawColor(Color.GREEN)
                    holder.unlockCanvasAndPost(canvas)
                }

                if (i == 2) {
                    val canvas = holder.lockCanvas(Rect(60, 60, 540, 540))
                    dumpCanvasRect(canvas)
                    canvas.drawColor(Color.BLUE)
                    holder.unlockCanvasAndPost(canvas)
                }

                //画圆形
                if (i == 3) {
                    val canvas = holder.lockCanvas(Rect(200, 200, 400, 400))
                    dumpCanvasRect(canvas)
                    paint.color = Color.argb(0x3F, 0xFF, 0xFF, 0xFF)
                    canvas.drawCircle(300f, 300f, 100f, paint)
                    holder.unlockCanvasAndPost(canvas)
                }

                if (i == 4) {
                    val canvas = holder.lockCanvas(Rect(250, 250, 350, 350))
                    dumpCanvasRect(canvas)
                    paint.color = Color.RED
                    canvas.drawText("$i", 300f, 300f, paint)
                    holder.unlockCanvasAndPost(canvas)
                }

                Thread.sleep(800)
            }
        }
    }

    private fun dumpCanvasRect(canvas: Canvas?) {
        if (canvas != null) {
            val rect = canvas.clipBounds
            Log.i(TAG, "left:" + rect.left + "  top:" + rect.top + "  right:" + rect.right + "  bottom:" + rect.bottom)
        }
    }

}