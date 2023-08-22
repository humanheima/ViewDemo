package com.hm.viewdemo.widget

import android.graphics.*
import android.graphics.drawable.GradientDrawable

/**
 * 动画渐变的drawable
 * Created by fengkeke on 2023/8/22
 */
class AnimatedGradientDrawable : GradientDrawable() {

    /**
     * 渐变颜色
     */
    var gradientColors: IntArray =
        intArrayOf(Color.BLUE, Color.RED)
        set(value) {
            field = value
            if (value.size < 2) throw IllegalArgumentException("colors must be >= 2")
            val step = 1f / (value.size - 1)
            positions = FloatArray(value.size) { i -> i * step }
        }

    /**
     * 圆角半径
     */
    var cornerRadius: Int = 0

    /**
     * 是否开启动画
     */
    var enableAnim: Boolean = true

    /**
     * 画笔样式
     */
    var paintStyle: Paint.Style = Paint.Style.FILL
        set(value) {
            field = value
            paint.style = value
        }

    /**
     * 画笔宽度
     */
    var paintWidth: Int = 0
        set(value) {
            field = value
            paint.strokeWidth = value.toFloat()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var linearGradient: LinearGradient? = null
    private val matrix = Matrix()
    private val STEP_SIZE = 10f

    private var positions: FloatArray = floatArrayOf(0f, 1f)
    private var currentTranslate = 0f


    init {
        paint.apply {
            style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()
        val radius = if (cornerRadius > 0) {
            cornerRadius
        } else {
            width.coerceAtMost(height) / 2
        }

        paint.shader = linearGradient
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        if (enableAnim) {
            currentTranslate += STEP_SIZE
            if (currentTranslate > width) {
                currentTranslate = -width.toFloat()
                revertGradientColors()
            }
            matrix.setTranslate(currentTranslate, 0f)
            linearGradient?.setLocalMatrix(matrix)
            invalidateSelf()
        }
    }

    override fun onBoundsChange(r: Rect?) {
        super.onBoundsChange(r)
        linearGradient =
            LinearGradient(
                0f,
                0f,
                r?.width()?.toFloat() ?: 0f,
                0f,
                gradientColors,
                positions,
                Shader.TileMode.CLAMP
            )
        currentTranslate = 0f
    }

    private fun revertGradientColors() {
        gradientColors = gradientColors.reversedArray()
        linearGradient = LinearGradient(
            0f,
            0f,
            bounds.width().toFloat(),
            0f,
            gradientColors,
            positions,
            Shader.TileMode.CLAMP
        )
    }

}