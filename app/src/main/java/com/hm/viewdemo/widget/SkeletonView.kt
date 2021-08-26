package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by dumingwei on 2021/8/24.
 *
 * Desc:
 */
class SkeletonView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val TAG: String = "SkeletonView"


    var paint: Paint

    var bgColor: Int = Color.WHITE

    //色块的颜色
    var rectColor: Int = Color.BLUE

    var topRectWidth: Int = 0

    //第一根线的宽度
    var topFirstLineWidth: Int = 0

    var topFirstLineHeight: Float = 40f
    var topSecondLineHeight: Float = 40f

    var topThirdLineHeight: Float = 60f

    //第二根线的宽度
    var topSecondLineWidth: Int = 0

    //第三根线的宽度
    var topThirdLineWidth: Float = 0f
    var topForthLineWidth: Int = 0
    var topFifthLineWidth: Int = 0
    var topSixLineWidth: Int = 0
    var topSevenLineWidth: Int = 0


    var startMargin = 12
    var endMargin = 12
    var dp16 = 16
    var dp20: Float = 20f

    var rectRadius: Float = 24f

    var rectF = RectF()

    var firstLineRectF = RectF()
    var secondLineRectF = RectF()

    var thirdLineRectF = RectF()

    var forthLineRectF = RectF()

    var fifthLineRectF = RectF()

    var sixthLineRectF = RectF()

    var seventhLineRectF = RectF()

    var bottomRectF = RectF()

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        topRectWidth = (measuredWidth - startMargin - endMargin - 2 * dp16) / 3

        topThirdLineWidth = topRectWidth / 3f * 2

        forthLineRectF.set(0f, 0f, topRectWidth * 2 / 3f, topFirstLineHeight)


        //3分之一
        val bottomRectWidth = (measuredWidth - startMargin - endMargin - dp20) / 3f

        val bottomRectHeight = bottomRectWidth

        bottomRectF.set(0f, 0f, bottomRectWidth, bottomRectHeight)
        fifthLineRectF.set(0f, 0f, bottomRectWidth * 2f, topFirstLineHeight * 2f)

        sixthLineRectF.set(0f, 0f, fifthLineRectF.width() * 4 / 5f, topFirstLineHeight)

        seventhLineRectF.set(0f, 0f, fifthLineRectF.width() * 2 / 3f, topFirstLineHeight)


        Log.i(TAG, "onMeasure: topRectWidth = $topRectWidth")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(bgColor)
        paint.color = rectColor
        if (topRectWidth > 0) {
            canvas.translate(startMargin.toFloat(), dp16.toFloat())
            rectF.set(0f, 0f, topRectWidth.toFloat(), topRectWidth.toFloat())

            firstLineRectF.set(0f, 0f, topRectWidth / 2f, topFirstLineHeight)

            secondLineRectF.set(0f, 0f, topRectWidth / 3f, topSecondLineHeight)

            thirdLineRectF.set(0f, 0f, topThirdLineWidth, topThirdLineHeight)

            for (j in 0 until 2) {

                for (i in 0 until 3) {
                    canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint)

                    canvas.translate(0f, topRectWidth + dp16.toFloat())
                    canvas.drawRoundRect(firstLineRectF, rectRadius, rectRadius, paint)

                    canvas.translate(0f, topFirstLineHeight + dp16.toFloat())
                    canvas.drawRoundRect(secondLineRectF, rectRadius, rectRadius, paint)

                    canvas.translate(dp16.toFloat() + topRectWidth, -topRectWidth - dp16.toFloat() - topFirstLineHeight - dp16.toFloat())

                }

                paint.color = Color.GRAY

                canvas.translate(-3 * (dp16.toFloat() + topRectWidth), topRectWidth + dp16.toFloat() + topFirstLineHeight + topSecondLineHeight + dp16.toFloat() + dp16.toFloat())

            }


            canvas.translate(0f, dp20)


            //绘制中间那条短线。
            canvas.translate((measuredWidth - topThirdLineWidth) / 2 - startMargin, 0f)
            canvas.drawRoundRect(thirdLineRectF, rectRadius, rectRadius, paint)

            canvas.translate(-((measuredWidth - topThirdLineWidth) / 2 - startMargin), dp20 * 3)

            //绘制横向的布局

            for (i in 0 until 2) {
                canvas.drawRoundRect(forthLineRectF, rectRadius, rectRadius, paint)
                canvas.translate(0f, dp20 * 3)

                canvas.drawRoundRect(bottomRectF, rectRadius, rectRadius, paint)

                //绘制右边的三条线

                canvas.translate(bottomRectF.width() + dp16, 0f)

                canvas.drawRoundRect(fifthLineRectF, rectRadius, rectRadius, paint)


                canvas.translate(0f, fifthLineRectF.height() + dp16)

                canvas.drawRoundRect(sixthLineRectF, rectRadius, rectRadius, paint)

                canvas.translate(0f, bottomRectF.height() - seventhLineRectF.height() - fifthLineRectF.height() - dp16)

                canvas.drawRoundRect(seventhLineRectF, rectRadius, rectRadius, paint)

                canvas.translate(-(bottomRectF.width() + dp16), bottomRectF.height() + dp20)
            }


        }
    }


}