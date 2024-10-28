package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R


/**
 * Created by p_dmweidu on 2024/10/26
 * Desc: 渐变环形进度条
 */

class GradientRingProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {
    // 圆环的背景颜色
    private var ringColor = Color.GRAY

    /**
     * 使用水平渐变的话，为true
     */
    var useLinearGradient = false

    //圆环的宽度
    private var ringWidth = 10

    // 当前进度
    var currentProgress: Int = 60

    // 最大进度
    var maxProgress: Int = 100

    // 画笔对象
    private val paint: Paint

    private val rectF = RectF()

    //颜色渐变器
    private var sg: SweepGradient? = null
    private var linearGradient: LinearGradient? = null

    var colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.RED)
    //private val colors = intArrayOf(Color.BLUE, Color.GREEN, Color.BLUE)

    //0 240 360
    //0 2, 3
    private val positions = floatArrayOf(0f, 0.7f, 0.8f, 1f)

    init {
        // 得到自定义资源数组
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView)
        ringColor = typedArray.getColor(R.styleable.RingProgressView_ringColor, ringColor)
        ringWidth = typedArray.getDimension(R.styleable.RingProgressView_ringWidth, 0f).toInt()
        currentProgress =
            typedArray.getInt(R.styleable.RingProgressView_currentProgress, currentProgress)
        maxProgress = typedArray.getColor(R.styleable.RingProgressView_maxProgress, maxProgress)
        typedArray.recycle()

        paint = Paint()
        // 抗锯齿
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND // 设置笔头为圆形
        paint.strokeWidth = ringWidth.toFloat()
        paint.color = ringColor
    }

    private val sweepAngel = 230f
    private val startAngel = 155f

    // 绘制
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = null
        paint.color = ringColor

        rectF.left = ringWidth / 2f
        rectF.top = ringWidth / 2f
        rectF.right = width - ringWidth / 2f
        rectF.bottom = height - ringWidth / 2f

        //先绘制背景色
        canvas.drawArc(rectF, startAngel, sweepAngel, false, paint)

        paint.shader = if (useLinearGradient) linearGradient else sg
        //绘制进度
        canvas.drawArc(rectF, startAngel, currentProgress * sweepAngel / maxProgress, false, paint)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            if (useLinearGradient) {
                if (linearGradient == null) {
                    linearGradient = LinearGradient(
                        0f,
                        0f,
                        w.toFloat(),
                        0f,
                        colors,
                        positions,
                        Shader.TileMode.CLAMP
                    )
                }
            } else {
                if (sg == null) {
                    sg = SweepGradient(w / 2f, h / 2f, colors, positions)
                    val matrix = Matrix()
                    matrix.setRotate(startAngel, w / 2f, h / 2f) // 旋转矩阵
                    sg?.setLocalMatrix(matrix)
                }
            }
        }
    }

}
