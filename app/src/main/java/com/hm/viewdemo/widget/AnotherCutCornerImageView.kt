package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.hm.viewdemo.R
import com.hm.viewdemo.extension.dp2pxFloat

/**
 * Created by p_dmweidu on 2024/9/12
 * Desc: 绘制异形ImageView，使用arcTo 比较复杂，参考 CutCornerImageView的实现，比较简单
 *
 * 使用arcTo 最后一个参数 forceMoveTo 不要传 true
 *
 *
 */
class AnotherCutCornerImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {


    private val TAG = "TestImageView"

    /**
     * path不会影响background！！！
     */
    private var path = Path()

    private var dp16 = 0f
    private var dp20 = 0f
    private var dp40 = 0f
    private var dp60 = 0f
    private var horizontalLine = 0f
    private var dp120 = 0f

    private var radius = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        radius = 40.dp2pxFloat(context)

        dp16 = 16.dp2pxFloat(context)
        dp20 = 20.dp2pxFloat(context)
        dp40 = 40.dp2pxFloat(context)

        dp60 = 60.dp2pxFloat(context)

        horizontalLine = 80.dp2pxFloat(context)
        dp120 = 120.dp2pxFloat(context)
        paint.setColor(resources.getColor(R.color.pink_500))
        paint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        //起点左上角: 0，0，
        //path.moveTo(0f, 0f)
        //第一条线，到右上角: w，0
        path.lineTo(w.toFloat(), 0f)


        //起点坐标
        var right = w.toFloat()
        //向下120dp
        var startY = height - dp120 - dp20


        path.lineTo(right, startY)

        path.arcTo(
            right - radius * 2,
            startY - radius,
            right,
            startY + radius,
            0f,
            90f,
            false
        )

        //减去直径，再减去横向向左的距离，得到第二个线的点
        right = right - radius - horizontalLine - radius

        startY += radius
//        //第二个线的点
        path.lineTo(right, startY)
        val left = right - radius
        val top = startY
        path.arcTo(left, top, left + radius * 2, top + radius * 2, -90f, -90f, false)

        val endLinePos = top + radius + dp20
        path.lineTo(left, endLinePos)

        path.arcTo(
            left - radius * 2,
            endLinePos - radius,
            left,
            endLinePos + radius,
            0f,
            90f,
            false
        )
        path.lineTo(0f, height.toFloat())
        path.lineTo(0f, 0f)
        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

}