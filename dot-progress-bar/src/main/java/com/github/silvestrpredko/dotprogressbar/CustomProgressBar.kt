// CustomProgressBar.kt
package com.github.silvestrpredko.dotprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by p_dmweidu on 2024/10/28
 * Desc:
 */
class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
    }

    private var circleRadius = 50f
    private var animator: ValueAnimator? = null

    init {
        startAnimation()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(50f, 100f, 50f).apply {
            duration = 1000 // 动画持续时间
            repeatCount = ValueAnimator.INFINITE // 无限重复
            addUpdateListener { animation ->
                circleRadius = animation.animatedValue as Float
                invalidate() // 重新绘制视图
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val spacing = width / 6 // 圆形间距

        // 绘制三个圆形
        canvas.drawCircle(width / 4, height / 2, circleRadius, paint)
        canvas.drawCircle(width / 2, height / 2, circleRadius, paint)
        canvas.drawCircle(3 * width / 4, height / 2, circleRadius, paint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel() // 停止动画
    }
}