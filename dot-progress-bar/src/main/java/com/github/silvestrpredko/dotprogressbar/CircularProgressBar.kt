package com.github.silvestrpredko.dotprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by p_dmweidu on 2024/10/28
 * Desc: 三个圆点的，无限进度条
 */
class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    
    private var circleSizes = floatArrayOf(50f, 50f, 50f) // 初始圆形大小
    private var maxSize = 100f // 最大圆形大小
    private var minSize = 30f // 最小圆形大小

    init {
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000 // 动画持续时间
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                circleSizes[0] = minSize + (maxSize - minSize) * animatedValue
                circleSizes[1] = minSize + (maxSize - minSize) * (1 - animatedValue)
                circleSizes[2] = minSize + (maxSize - minSize) * animatedValue
                invalidate() // 重绘视图
            }
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        // 计算圆形的位置
        val spacing = 20f
        val startX = (width - (circleSizes.sum() + spacing * 2)) / 2

        for (i in circleSizes.indices) {
            val cx = startX + i * (circleSizes[i] + spacing) + circleSizes[i] / 2
            val cy = height / 2
            paint.color = when (i) {
                0 -> 0xFF6200EE.toInt() // 圆形1颜色
                1 -> 0xFF03DAC5.toInt() // 圆形2颜色
                else -> 0xFFFFC107.toInt() // 圆形3颜色
            }
            canvas.drawCircle(cx, cy, circleSizes[i] / 2, paint)
        }
    }
}