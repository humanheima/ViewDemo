package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log

/**
 * Created by p_dmweidu on 2024/10/22
 * Desc: 指定渐变颜色和渐变方向的TextView
 */
class GradientTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {


    private val TAG = "GradientTextView"

    private var linearGradient: LinearGradient? = null


    var direction: GradientDirection = GradientDirection.LEFT_TO_RIGHT

    val colors = intArrayOf(Color.RED, Color.BLUE) // 红到绿再到蓝

    // 定义渐变方向
    enum class GradientDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP,
        LEFT_TO_RIGHT_TOP_TO_BOTTOM,
        RIGHT_TO_LEFT_TOP_TO_BOTTOM,
        LEFT_TO_RIGHT_BOTTOM_TO_TOP,
        RIGHT_TO_LEFT_BOTTOM_TO_TOP
    }

    private fun setGradientDirection(direction: GradientDirection) {
        val width = width
        val height = height
        Log.d(TAG, "setGradientDirection: width = $width , height = $height")

        linearGradient = when (direction) {
            GradientDirection.LEFT_TO_RIGHT -> LinearGradient(
                0f,
                0f,
                width.toFloat(),
                0f,
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.RIGHT_TO_LEFT -> LinearGradient(
                width.toFloat(),
                0f,
                0f,
                0f,
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.TOP_TO_BOTTOM -> LinearGradient(
                0f,
                0f,
                0f,
                height.toFloat(),
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.BOTTOM_TO_TOP -> LinearGradient(
                0f,
                height.toFloat(),
                0f,
                0f,
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.LEFT_TO_RIGHT_TOP_TO_BOTTOM -> LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.RIGHT_TO_LEFT_TOP_TO_BOTTOM -> LinearGradient(
                width.toFloat(),
                0f,
                0f,
                height.toFloat(),
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.LEFT_TO_RIGHT_BOTTOM_TO_TOP -> LinearGradient(
                0f,
                height.toFloat(),
                width.toFloat(),
                0f,
                colors,
                null,
                Shader.TileMode.CLAMP
            )

            GradientDirection.RIGHT_TO_LEFT_BOTTOM_TO_TOP -> LinearGradient(
                width.toFloat(),
                height.toFloat(),
                0f,
                0f,
                colors,
                null,
                Shader.TileMode.CLAMP
            )
        }

        paint.color = Color.TRANSPARENT
        paint.shader = linearGradient
        //invalidate() // 刷新视图以应用新的渐变
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setGradientDirection(direction) // 视图大小变化时更新渐变方向
    }
}