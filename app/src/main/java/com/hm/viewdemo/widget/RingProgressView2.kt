package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Author        Hule  hu.le@cesgroup.com.cn
 * Date          2017/6/12 13:30
 * Description:  TODO: 自定义环形进度条
 */
class RingProgressView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {
    // 圆环的颜色
    private var ringColor = Color.GRAY
    //private var ringColor = Color.TRANSPARENT

    // 圆环进度的颜色
    private var ringProgressColor = Color.CYAN

    //圆环的宽度
    private var ringWidth = 10

    // 当前进度
    var currentProgress: Int = 60

    // 最大进度
    var maxProgress: Int = 100

    // 得到控件的宽度
    private var width = 0

    // 画笔对象
    private val paint: Paint

    private val rectF = RectF()

    //颜色渐变器
    private var sg: SweepGradient? = null

    private val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.RED)
    //private val colors = intArrayOf(Color.BLUE, Color.GREEN, Color.BLUE)

    //0 240 360
    //0 2, 3
    private val positions = floatArrayOf(0f, 0.7f, 0.98f, 1f)

    init {
        // 得到自定义资源数组
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView)
        ringColor = typedArray.getColor(R.styleable.RingProgressView_ringColor, ringColor)
        ringProgressColor =
            typedArray.getColor(R.styleable.RingProgressView_ringProgressColor, ringProgressColor)
        ringWidth = typedArray.getDimension(R.styleable.RingProgressView_ringWidth, 0f).toInt()
        currentProgress =
            typedArray.getInt(R.styleable.RingProgressView_currentProgress, currentProgress)
        maxProgress = typedArray.getColor(R.styleable.RingProgressView_maxProgress, maxProgress)
        typedArray.recycle()

        paint = Paint()
        // 抗锯齿
        paint.isAntiAlias = true
        paint.color = ringColor
    }

    // 测量
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth
    }

    private val sweepAngel = 230f
    private val startAngel = 155f

    // 绘制
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = null
        // 1. 计算圆心坐标及半径
        val centerX = width / 2f
        val centerY = width / 2f
        val radius = width / 2f - ringWidth / 2f

        // 2. 画圆环
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = ringWidth.toFloat()
        paint.strokeCap = Paint.Cap.ROUND // 设置笔头为圆形

        rectF.left = ringWidth / 2f
        rectF.top = ringWidth / 2f
        rectF.right = width - ringWidth / 2f
        rectF.bottom = height - ringWidth / 2f

        canvas.drawArc(rectF, startAngel, sweepAngel, false, paint)

        if (sg == null) {
            sg = SweepGradient(centerX, centerY, colors, positions)
            val matrix: Matrix = Matrix()
            matrix.setRotate(startAngel, centerX, centerY) // 旋转矩阵
            sg?.setLocalMatrix(matrix)
            paint.shader = sg
        }

        canvas.drawArc(rectF, startAngel, currentProgress * sweepAngel / maxProgress, false, paint)

    }

}
