package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-01.
 * Desc:
 */
class EraserView_SRCOUT @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint = Paint()

    private var bmpDST: Bitmap
    private var bmpSRC: Bitmap
    private var bmpText: Bitmap
    private var mPath: Path

    private var mPreX: Float = 0f
    private var mPreY: Float = 0f

    private var dstCanvas: Canvas
    private var xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 45f

        val options = BitmapFactory.Options()
        // options.inSampleSize = 2
        bmpSRC = BitmapFactory.decodeResource(resources, R.drawable.ic_dog, options)

        /*bmpSRC = ImageUtil.getSampledBitmapFromResource(
                resources,
                R.drawable.ic_dog,
                ScreenUtil.dpToPx(context, 200),
                ScreenUtil.dpToPx(context, 200)
        )*/

        //空白图像
        bmpDST = Bitmap.createBitmap(bmpSRC.width, bmpSRC.height, Bitmap.Config.ARGB_8888)

        bmpText = BitmapFactory.decodeResource(resources, R.drawable.guaguaka_text, options)

        //使用空白图像创建一个画布
        dstCanvas = Canvas(bmpDST)

        mPath = Path()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath.moveTo(event.x, event.y)
                mPreX = event.x
                mPreY = event.y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
            }

            MotionEvent.ACTION_UP -> {
                //do nothing
            }
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    private val rectF = RectF(0f, 0f, bmpDST.width.toFloat(), bmpDST.height.toFloat())

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bmpText, null, rectF, paint)

        //把路径画到空白图像上
        val layerId = canvas.saveLayer(0f, 0f, width * 1.0f, height * 1.0f, null, Canvas.ALL_SAVE_FLAG)
        dstCanvas.drawPath(mPath, paint)

        canvas.drawBitmap(bmpDST, 0f, 0f, paint)

        paint.xfermode = xfermode
        canvas.drawBitmap(bmpSRC, 0f, 0f, paint)

        paint.xfermode = null

        canvas.restoreToCount(layerId)

    }

}