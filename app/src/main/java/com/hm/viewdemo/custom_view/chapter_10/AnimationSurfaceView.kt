package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-15.
 * Desc:
 */
class AnimationSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr) {

    private var surfaceHolder: SurfaceHolder? = null

    private var flag = false

    private var bitmapBg: Bitmap? = null

    //屏幕宽高
    private var mSurfaceWidth: Int = 0
    private var mSurfaceHeight: Int = 0

    private var mBitmapPosX = 0f//开始绘制的图片的X坐标
    private var mCanvas: Canvas? = null
    private var thread: Thread? = null

    // 背景移动状态
    private enum class State {
        LEFT,
        RIGHT
    }

    private var state = State.LEFT

    // 背景画布移动步伐.
    private val BITMAP_STEP = 10

    init {
        surfaceHolder = holder
        surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                //do nothing

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                flag = false
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                flag = true
                startAnimation()
            }
        })
    }

    private fun startAnimation() {
        mSurfaceWidth = width
        mSurfaceHeight = height

        val mWidth = mSurfaceWidth * 3 / 2

        /***
         * 将图片缩放到屏幕的3/2倍.
         */
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.scenery)
        bitmapBg = Bitmap.createScaledBitmap(bitmap, mWidth, mSurfaceHeight, true)
        thread = kotlin.concurrent.thread {

            while (flag) {

                mCanvas = surfaceHolder?.lockCanvas()
                drawView()
                surfaceHolder?.unlockCanvasAndPost(mCanvas)

                Thread.sleep(50)
            }
        }
    }

    private fun drawView() {
        mCanvas?.let {
            it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            if (bitmapBg != null) {
                it.drawBitmap(bitmapBg!!, mBitmapPosX, 0f, null)
            }
            when (state) {

                State.LEFT -> {
                    mBitmapPosX -= BITMAP_STEP
                }

                State.RIGHT -> {
                    mBitmapPosX += BITMAP_STEP
                }
            }
            if (mBitmapPosX <= -mSurfaceWidth / 2) {
                state = State.RIGHT
            }
            if (mBitmapPosX >= 0) {
                state = State.LEFT
            }

        }

    }

}