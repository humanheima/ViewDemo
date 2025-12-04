package com.example.viewdemo.placeholder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * 固定高度的占位符View
 * 根据设定的高度自动计算并绘制尽可能多的横条
 */
class PlaceholderView2 @JvmOverloads constructor(
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

    // 预设的宽度比例（相对于View宽度的百分比）
    private val barWidthRatios = listOf(0.9f, 0.7f, 0.85f, 0.6f, 0.8f, 0.65f, 0.75f, 0.55f, 0.82f, 0.68f)

    // 实际需要绘制的横条数量（根据高度计算）
    private var actualBarCount = 0

    init {
        // 将dp值转换为px
        val density = context.resources.displayMetrics.density
        barHeight = 12 * density
        barSpacing = 8 * density
        cornerRadius = 6 * density
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateBarCount()
    }

    /**
     * 根据当前View高度计算最多能绘制多少条横条
     */
    private fun calculateBarCount() {
        val availableHeight = (height - paddingTop - paddingBottom).toFloat()
        
        if (availableHeight <= 0 || barHeight <= 0) {
            actualBarCount = 0
            return
        }

        // 计算最多能放下多少条
        // 公式：n * barHeight + (n-1) * barSpacing <= availableHeight
        // 解得：n <= (availableHeight + barSpacing) / (barHeight + barSpacing)
        val maxCount = ((availableHeight + barSpacing) / (barHeight + barSpacing)).toInt()
        
        // 通过取模的方式循环使用预设的宽度比例数组，不限制数量
        actualBarCount = maxCount.coerceAtLeast(0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas ?: return

        if (actualBarCount <= 0) return

        var currentY = paddingTop.toFloat()
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()

        // 绘制计算出的横条数量
        repeat(actualBarCount) { index ->
            val barWidth = contentWidth * barWidthRatios[index % barWidthRatios.size]
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
        calculateBarCount()
        invalidate()
    }

    /**
     * 设置条之间的间距（dp）
     */
    fun setBarSpacing(spacingDp: Float) {
        val density = context.resources.displayMetrics.density
        barSpacing = spacingDp * density
        calculateBarCount()
        invalidate()
    }

    /**
     * 设置圆角半径（dp）
     */
    fun setCornerRadius(radiusDp: Float) {
        val density = context.resources.displayMetrics.density
        cornerRadius = radiusDp * density
        invalidate()
    }

    /**
     * 获取当前实际绘制的横条数量
     */
    fun getActualBarCount(): Int = actualBarCount
}