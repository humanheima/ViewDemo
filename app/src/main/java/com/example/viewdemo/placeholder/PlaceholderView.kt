package com.example.viewdemo.placeholder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Created by p_dmweidu on 2025/12/4
 * Desc: 自定义占位符View，用于显示多行不同长度的灰色条,模拟加载状态下的内容占位符效果
 */

class PlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 画笔，用于绘制灰色条
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E5E5E5") // 浅灰色
        style = Paint.Style.FILL
    }

    // 每行条的高度（dp转px后的值）
    private var barHeight = 0f

    // 条之间的间距（dp转px后的值）
    private var barSpacing = 0f

    // 圆角半径（dp转px后的值）
    private var cornerRadius = 0f

    // 每行条的宽度比例（相对于View宽度的百分比）
    private val barWidthRatios = listOf(0.85f, 0.65f, 0.9f, 0.45f, 0.7f)

    init {
        // 将dp值转换为px
        val density = context.resources.displayMetrics.density
        barHeight = 12 * density
        barSpacing = 8 * density
        cornerRadius = 6 * density
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas ?: return

        var currentY = paddingTop.toFloat()
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()

        // 绘制每一行的灰色条
        barWidthRatios.forEach { widthRatio ->
            val barWidth = contentWidth * widthRatio
            val rect = RectF(
                paddingLeft.toFloat(),
                currentY,
                paddingLeft + barWidth,
                currentY + barHeight
            )

            // 绘制圆角矩形
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

            // 移动到下一行位置
            currentY += barHeight + barSpacing
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 计算总高度：所有条的高度 + 间距 + padding
        val totalHeight = (barWidthRatios.size * barHeight +
                (barWidthRatios.size - 1) * barSpacing +
                paddingTop + paddingBottom).toInt()

        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            totalHeight
        )
    }

    /**
     * 设置占位符的颜色
     */
    fun setPlaceholderColor(color: Int) {
        paint.color = color
        invalidate()
    }

    /**
     * 设置条的高度（dp）
     */
    fun setBarHeight(heightDp: Float) {
        val density = context.resources.displayMetrics.density
        barHeight = heightDp * density
        requestLayout()
    }

    /**
     * 设置条之间的间距（dp）
     */
    fun setBarSpacing(spacingDp: Float) {
        val density = context.resources.displayMetrics.density
        barSpacing = spacingDp * density
        requestLayout()
    }

    /**
     * 设置圆角半径（dp）
     */
    fun setCornerRadius(radiusDp: Float) {
        val density = context.resources.displayMetrics.density
        cornerRadius = radiusDp * density
        invalidate()
    }
}