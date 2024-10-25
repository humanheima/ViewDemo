package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.hm.viewdemo.extension.dp2pxFloat

/**
 * Created by p_dmweidu on 2024/10/22
 * Desc: 左下角到右上角的斜线
 */
class DiagonalLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "DiagonalLineView"

    // 设置线条颜色
    var lineColor = 0x80C1C1FF.toInt()

    var gap = 0f

    private val paint: Paint = Paint().apply {
        strokeWidth = 1.dp2pxFloat(context)
        isAntiAlias = true // 启用抗锯齿
        color = lineColor
        setStrokeCap(Paint.Cap.ROUND)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 获取视图的宽度和高度
        val width = width.toFloat()
        val height = height.toFloat()

        if (gap >= width || gap >= height) {
            Log.d(TAG, "onDraw: gap=$gap width=$width height=$height")
            return
        }

        // 绘制从左下角到右上角的斜线
        canvas.drawLine(gap, height - gap, width - gap, gap, paint)
    }
}