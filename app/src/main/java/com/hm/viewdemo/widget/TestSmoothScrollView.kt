package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller

import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2017/2/26.
 */
class TestSmoothScrollView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var scroller: Scroller = Scroller(context)
    private val paint = Paint()
    private var color: Int = 0

    init {
        color = context.resources.getColor(R.color.colorAccent)
        paint.color = color
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(color)
        paint.color = context.resources.getColor(R.color.colorPrimary)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    /**
     * 使用 scroller滚动
     *
     * @param destX 在水平方滚动到的目的地
     * @param destY 竖直方向上滚动的目的地
     */
    fun smoothScrollTo(destX: Int, destY: Int) {

        //要滚动的距离
        val deltaX = destX - scrollX
        val deltaY = destY - scrollY

        scroller.startScroll(scrollX, scrollY, deltaX, deltaY, 1000)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }
}
