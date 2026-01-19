package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

/**
 * 自定义ImageView，实现图片以中心竖直轴向屏幕内弯曲的效果
 */
class CurvedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    private val TAG = "CurvedImageView"

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var curvature = 0.5f // 弯曲程度，0为无弯曲，1为最大弯曲
    private var bitmap: Bitmap? = null
    private val matrix = Matrix()
    private val camera = Camera()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    /**
     * 设置弯曲程度
     * @param curvature 弯曲程度，0-1之间，0为无弯曲，1为最大弯曲
     */
    fun setCurvature(curvature: Float) {
        this.curvature = curvature.coerceIn(0f, 1f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        if (bitmap == null || bitmap!!.isRecycled) {
            createBitmapFromDrawable(drawable)
        }

        val bmp = bitmap
        if (bmp == null) {
            Log.d(TAG, "onDraw: bitmap is null")
            return
        }

        drawCurvedBitmap(canvas, bmp)
    }

    private fun createBitmapFromDrawable(drawable: android.graphics.drawable.Drawable) {
        val width = width
        val height = height
        if (width <= 0 || height <= 0) {
            Log.d(TAG, "createBitmapFromDrawable: width or height is zero")
            return
        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(bitmap!!)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(tempCanvas)
    }

    private fun drawCurvedBitmap(canvas: Canvas, sourceBitmap: Bitmap) {
        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2f

        // 增加分段数量以获得更平滑的效果
        val segments = 100
        val segmentWidth = width / segments

        for (i in 0 until segments) {
            val left = i * segmentWidth
            val right = (i + 1) * segmentWidth
            val segmentCenterX = (left + right) / 2f

            // 计算该段相对于中心的距离比例
            val distanceFromCenter = (segmentCenterX - centerX) / centerX

            // 使用更平滑的曲线函数来计算旋转角度
            val maxRotationAngle = 45f * curvature // 减小最大旋转角度使效果更自然
            val rotationAngle =
                distanceFromCenter * maxRotationAngle * sin(PI * abs(distanceFromCenter) / 2).toFloat()

            // 计算Z轴偏移，使用二次函数实现更自然的弯曲
            val maxZOffset = 150f * curvature
            val zOffset = maxZOffset * distanceFromCenter * distanceFromCenter

            // 准备矩阵变换
            matrix.reset()
            camera.save()

            // 应用3D变换（使用正值使两边向屏幕内弯曲）
            camera.translate(0f, 0f, zOffset)
            camera.rotateY(rotationAngle)
            camera.getMatrix(matrix)
            camera.restore()

            // 调整变换中心点
            matrix.preTranslate(-segmentCenterX, -height / 2f)
            matrix.postTranslate(segmentCenterX, height / 2f)

            // 创建该段的源矩形和目标矩形
            val srcRect = Rect(left.toInt(), 0, right.toInt(), height.toInt())
            val dstRect = RectF(left, 0f, right, height)

            canvas.save()
            canvas.clipRect(dstRect)
            canvas.concat(matrix)

            // 绘制该段
            canvas.drawBitmap(sourceBitmap, srcRect, dstRect, paint)
            canvas.restore()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bitmap?.recycle()
        bitmap = null
    }
}

