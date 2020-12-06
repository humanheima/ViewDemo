package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by dumingwei on 2019-08-16.
 * Desc: 蜘蛛网图
 *
 * 为了方便，将 count写死为6
 */
class SpiderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "SpiderView"


    private val DEFAULT_SIZE = 200

    //网格最大半径
    private var radius: Float = 0f

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private var radarPaint: Paint = Paint()
    private var valuePaint: Paint = Paint()


    //蜘蛛网的环数
    private var count: Int = 6

    //结果是弧度
    private var angle = Math.PI * 2 / count

    private var maxValue = count

    private var data: Array<Double> = arrayOf(1.4, 5.0, 1.2, 6.0, 4.4, 4.8)

    init {

        radarPaint.style = Paint.Style.STROKE
        radarPaint.strokeWidth = 3f
        radarPaint.color = Color.GREEN

        valuePaint.color = Color.BLUE
        valuePaint.style = Paint.Style.FILL

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecModel = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightSpecModel = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecModel == View.MeasureSpec.AT_MOST && heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE)
        } else if (widthSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize)
        } else if (heightSpecModel == View.MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_SIZE)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Math.min(w, h) / 2 * 0.9f
        centerX = w / 2f
        centerY = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制蜘蛛网格
        drawPolygon(canvas)
        //画网格中的线
        drawLines(canvas)
        //画数据图
        drawRegion(canvas)
    }

    /**
     * 可以试着现将画布的原点移动到中心
     */
    private fun drawPolygon(canvas: Canvas) {
        val path = Path()
        //网格之间的间距
        val r = radius / count

        for (i in 1..count) {
            val curR = r * i//每环的半径
            //重置path
            path.reset()
            //移动path到水平起点
            path.moveTo(centerX + curR, centerY)
            for (j in 0 until count) {
                val x = centerX + curR * Math.cos(angle * j).toFloat()
                val y = centerY + curR * Math.sin(angle * j).toFloat()
                path.lineTo(x, y)
            }
            path.close()
            canvas.drawPath(path, radarPaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        val path = Path()
        for (i in 0 until count) {
            path.reset()
            path.moveTo(centerX, centerY)
            val x = centerX + radius * Math.cos(angle * i).toFloat()
            val y = centerY + radius * Math.sin(angle * i).toFloat()
            path.lineTo(x, y)
            canvas.drawPath(path, radarPaint)
        }
    }

    private fun drawRegion(canvas: Canvas) {
        val path = Path()
        valuePaint.alpha = 127
        for (i in 0 until count) {
            val percent: Double = data[i] * 1.0 / maxValue


            val x = (centerX + radius * Math.cos(angle * i) * percent).toFloat()
            val y = (centerY + radius * Math.sin(angle * i) * percent).toFloat()

            //绘制小圆点
            canvas.drawCircle(x, y, 10f, valuePaint)

            Log.i(TAG, "drawRegion: $x,$y")

            if (i == 0) {
                path.moveTo(x, centerY)
            } else {
                path.lineTo(x, y)
            }
        }
        valuePaint.style = Paint.Style.FILL_AND_STROKE
        //绘制填充区域
        canvas.drawPath(path, valuePaint)
    }

}