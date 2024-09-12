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
 * Desc:
 */
class TestImageView @JvmOverloads constructor(
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
    private var dp80 = 0f
    private var dp120 = 0f

    private var radius = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        radius = 40.dp2pxFloat(context)

        dp16 = 16.dp2pxFloat(context)
        dp20 = 20.dp2pxFloat(context)
        dp40 = 40.dp2pxFloat(context)

        dp60 = 60.dp2pxFloat(context)

        dp80 = 80.dp2pxFloat(context)
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

        //这时候，路径的终点是 (right - radius, startY + radius)

        //减去直径，再减去横向向左的距离，得到第二个线的点
        right = right - radius - dp60 - radius

        //第二个线的点
        path.lineTo(right, startY + radius)


        var left = right - radius

        var top = startY + radius

        path.arcTo(left, top, left + radius * 2, top + radius * 2, -90f, -90f, false)

        path.lineTo(left, top + radius + dp20)

        path.arcTo(left - radius * 2, top + dp20, left, top + radius * 2 + dp20, 0f, 90f, false)

        path.lineTo(0f, height.toFloat())

        path.lineTo(0f, 0f)

        path.close()

    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.onDraw(canvas)
        //canvas.drawPath(path, paint)
    }

}