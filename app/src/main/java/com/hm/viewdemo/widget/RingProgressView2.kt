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
    private var ringColor = 0xFFEACE

    // 圆环进度的颜色
    private var ringProgressColor = 0xFF6000

    //圆环的宽度
    private var ringWidth = 10

    // 字体大小
    private var textSize = 20

    // 字体颜色
    private var textColor = -0xffff01

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

    private var linearGradient: LinearGradient? = null

    //private val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
    private val colors = intArrayOf(Color.BLUE, Color.GREEN)

    // 默认的构造方法，一般取这3个就够用了
    init {
        // 得到自定义资源数组
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView)
        ringColor = typedArray.getColor(R.styleable.RingProgressView_ringColor, ringColor)
        ringProgressColor =
            typedArray.getColor(R.styleable.RingProgressView_ringProgressColor, ringProgressColor)
        ringWidth =
            typedArray.getDimension(R.styleable.RingProgressView_ringWidth, dip2px(10).toFloat())
                .toInt()
        textSize = typedArray.getDimension(
            R.styleable.RingProgressView_rps_text_size,
            dip2px(20).toFloat()
        ).toInt()
        textColor = typedArray.getColor(R.styleable.RingProgressView_rps_text_color, textColor)
        currentProgress =
            typedArray.getInt(R.styleable.RingProgressView_currentProgress, currentProgress)
        maxProgress = typedArray.getColor(R.styleable.RingProgressView_maxProgress, maxProgress)
        typedArray.recycle()

        paint = Paint()
        // 抗锯齿
        paint.isAntiAlias = true
    }

    // 测量
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth
    }

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
        paint.color = ringColor
        //canvas.drawCircle(centerX, centerY, radius, paint)


        // 3. 画圆弧
        //rectF[ringWidth / 2f, ringWidth / 2f, width - ringWidth / 2f] = width - ringWidth / 2f

        rectF.left = ringWidth / 2f
        rectF.top = ringWidth / 2f
        rectF.right = width - ringWidth / 2f
        rectF.bottom = height - ringWidth / 2f

        //paint.color = ringProgressColor

        canvas.drawArc(rectF, 150f, 240f, false, paint)

//        if (sg == null) {
//            sg = SweepGradient(centerX, centerY, colors, null)
//            val matrix: Matrix = Matrix()
//            matrix.setRotate(150f, centerX, centerY) // 旋转矩阵
//            sg?.setLocalMatrix(matrix)
//            paint.shader = sg
//
//        }

        if (linearGradient == null) {
            linearGradient = LinearGradient(0f, 0f, width.toFloat(), 0f, colors, null, Shader.TileMode.CLAMP)
            //linearGradient = LinearGradient(0f, 0f, 100f, 0f, colors, null, Shader.TileMode.CLAMP)
            paint.shader = linearGradient

        }

        canvas.drawArc(rectF, 150f, currentProgress * 240f / maxProgress, false, paint)

    }

    // 布局
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    /**
     * 把dp转换成px
     *
     * @param dipValue
     * @return
     */
    private fun dip2px(dipValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
