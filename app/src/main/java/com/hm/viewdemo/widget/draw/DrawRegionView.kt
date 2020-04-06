package com.hm.viewdemo.widget.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by dumingwei on 2019-08-17.
 * Desc:
 */
class DrawRegionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        private const val DEFAULT_SIZE = 200

    }

    private var paint = Paint()


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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //drawRegionOne(canvas)
        drawRegionTwo(canvas)
        //testUnion(canvas)
        //testRegionOperation(canvas)
    }

    private fun drawRegionOne(canvas: Canvas) {
        val ovalPath = Path()
        val rectF = RectF(50f, 50f, 200f, 500f)

        ovalPath.addOval(rectF, Path.Direction.CCW)

        val rgn = Region()
        rgn.setPath(ovalPath, Region(50, 50, 200, 200))

        drawRegion(rgn, canvas)
    }

    private fun drawRegionTwo(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        val ovalPath = Path()
        val rectF = RectF(50f, 50f, 200f, 500f)
        ovalPath.addOval(rectF, Path.Direction.CCW)

        val rgn = Region()
        rgn.setPath(ovalPath, Region(50, 50, 200, 500))

        drawRegion(rgn, canvas)
    }

    private fun testUnion(canvas: Canvas) {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        val region = Region(10, 10, 200, 200)
        region.union(Rect(10, 10, 50, 300))
        drawRegion(region, canvas)
    }


    //水平的矩形
    private val rect1 = Rect(100, 100, 400, 200)

    private val rect2 = Rect(200, 0, 300, 300)

    private fun testRegionOperation(canvas: Canvas) {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRect(rect1, paint)
        canvas.drawRect(rect2, paint)

        val region1 = Region(rect1)
        val region2 = Region(rect2)

        //取交集
        //region1.op(region2, Region.Op.INTERSECT)

        //region1.op(region2, Region.Op.REPLACE)

        //region1.op(region2, Region.Op.DIFFERENCE)

        region1.op(region2, Region.Op.REVERSE_DIFFERENCE)

        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL

        drawRegion(region1, canvas)
    }

    private fun drawRegion(rgn: Region, canvas: Canvas) {
        val iterator = RegionIterator(rgn)
        val r = Rect()

        while (iterator.next(r)) {
            canvas.drawRect(r, paint)
        }
    }
}