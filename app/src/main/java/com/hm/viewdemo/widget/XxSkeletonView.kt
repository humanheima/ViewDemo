package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by dumingwei on 2021/8/24.
 *
 * Desc:
 */
class XxSkeletonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val TAG: String = "SkeletonView"


    var paint: Paint

    /**
     * 上半部分颜色
     */
    var topHalfBgColor: Int = 0

    /**
     * 下半部分部分颜色
     */
    var bottomHalfBgColor: Int = 0

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


    var startMargin = 0f
    var endMargin = 0f
    var dp40 = 0f
    var dp20: Float = 20f

    /**
     * 左边圆形半径
     */
    var radius: Float = 0f

    /**
     * 第一个绘制的图形距离顶部的距离
     */
    var topMargin = 0f
    var dp4 = 0f
    var dp12 = 0f

    /**
     * 正常两根线之间的间距
     */
    var dp16 = 0f
    var dp32 = 0f
    var dp48 = 0f
    var dp50 = 0f

    var dp56 = 0f

    var dp84 = 0f

    /**
     * 线条的高度
     */
    var lineHeight: Float = 0f


    /**
     * 上半部分，最后一根线条，距离底部上半部分底部的高度
     */
    var topPartBottomAreaHeight = 0f


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


    var shapeColor: Int = 0

    /**
     * 上半部灰色区域总的高度
     */
    var topAreaTotalHeight = 0f

    /**
     * 下半部分总的高度
     */
    var bottomAreaTotalHeight = 0f

    var totalTranslateX = 0f
    var totalTranslateY = 0f

    init {

        paint = Paint(Paint.ANTI_ALIAS_FLAG)

        topMargin = ScreenUtil.dpToPxFloat(context, 8)
        dp4 = ScreenUtil.dpToPxFloat(context, 4)
        dp12 = ScreenUtil.dpToPxFloat(context, 12)
        dp16 = ScreenUtil.dpToPxFloat(context, 16)
        dp32 = ScreenUtil.dpToPxFloat(context, 32)
        dp40 = ScreenUtil.dpToPxFloat(context, 40)
        dp48 = ScreenUtil.dpToPxFloat(context, 48)
        dp50 = ScreenUtil.dpToPxFloat(context, 50)
        dp56 = ScreenUtil.dpToPxFloat(context, 56)
        dp84 = ScreenUtil.dpToPxFloat(context, 84)

        radius = ScreenUtil.dpToPxFloat(context, 24)
        lineHeight = ScreenUtil.dpToPxFloat(context, 16)
        topPartBottomAreaHeight = ScreenUtil.dpToPxFloat(context, 66)

        startMargin = ScreenUtil.dpToPxFloat(context, 16)
        endMargin = ScreenUtil.dpToPxFloat(context, 16)

        topHalfBgColor = ContextCompat.getColor(context, R.color.top_half_bg)
        bottomHalfBgColor = ContextCompat.getColor(context, R.color.bottom_half_bg)
        shapeColor = ContextCompat.getColor(context, R.color.shape_color)

        //上半部的区域，高度写死
        topAreaTotalHeight = topMargin + dp16 * 7 + dp48 + dp50

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        totalTranslateX = 0f
        totalTranslateY = 0f
        paint.color = topHalfBgColor
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), topAreaTotalHeight, paint)

        canvas.translate(startMargin, topMargin)

        totalTranslateX += startMargin
        totalTranslateY += topMargin

        paint.color = shapeColor
        //绘制左边的圆形
        canvas.drawCircle(radius, radius, radius, paint)

        val firstLineLeft = 2 * radius + dp12
        //第一根线的宽度
        val firstLineWidth: Float = measuredWidth - dp16 - radius - radius - dp12 - dp16

        val secondLineWidth: Float = measuredWidth - dp16 - radius - radius - dp12 - dp84

        //绘制第一根线
        canvas.drawRoundRect(
            firstLineLeft,
            0f,
            firstLineLeft + firstLineWidth,
            dp16,
            dp4,
            dp4,
            paint
        )


        //向下移动16dp
        canvas.translate(0f, dp16)
        //向下移动16dp，这个是间距
        canvas.translate(0f, dp16)

        totalTranslateY += dp16
        totalTranslateY += dp16


        //绘制第2根线
        canvas.drawRoundRect(
            firstLineLeft,
            0f,//距离上面16dp
            firstLineLeft + secondLineWidth,
            dp16,
            dp4,
            dp4,
            paint
        )

        //向下移动16dp
        canvas.translate(0f, dp16)
        //向下移动48dp，这个是间距
        canvas.translate(0f, dp48)

        totalTranslateY += dp16
        totalTranslateY += dp48


        //绘制第3根线
        canvas.drawRoundRect(
            0f,
            0f,//距离上面16dp
            measuredWidth - dp16 - dp16,
            dp16,
            dp4,
            dp4,
            paint
        )

        //向下移动16dp
        canvas.translate(0f, dp16)
        //向下移动16dp，这个是间距
        canvas.translate(0f, dp16)

        totalTranslateY += dp16
        totalTranslateY += dp16

        //绘制第4根线
        canvas.drawRoundRect(
            0f,
            0f,//距离上面16dp
            measuredWidth - dp16 - dp16,
            dp16,
            dp4,
            dp4,
            paint
        )

        //回到控件左上角
        canvas.translate(-totalTranslateX, -totalTranslateY)

        //canvas.drawRect(0f,0f,10f,10f,paint)

        bottomAreaTotalHeight = measuredHeight - topAreaTotalHeight
        if (bottomAreaTotalHeight <= 0) {
            return
        }
        canvas.translate(0f, topAreaTotalHeight - dp16)
        totalTranslateY += (topAreaTotalHeight - dp16)

        paint.color = bottomHalfBgColor

        //绘制下半部分的圆角
        canvas.drawRoundRect(
            0f,
            0f,//距离上面16dp
            measuredWidth.toFloat(),
            dp16 + dp16,
            dp16,
            dp16,
            paint
        )

        canvas.translate(0f, dp16)
        totalTranslateY += dp16

        //绘制下半部分的背景
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), bottomAreaTotalHeight - dp16, paint)


        canvas.translate(0f, dp40)
        totalTranslateY += dp40

        if (totalTranslateY >= measuredHeight) {
            return
        }
        paint.color = shapeColor
        //绘制第5根线
        canvas.drawRoundRect(
            dp16,
            0f,//距离上面16dp
            measuredWidth - dp16,
            dp16,
            dp4,
            dp4,
            paint
        )

        canvas.translate(0f, dp32)
        totalTranslateY += dp32

        if (totalTranslateY >= measuredHeight) {
            return
        }

        //绘制第6根线
        canvas.drawRoundRect(
            dp16,
            0f,//距离上面16dp
            measuredWidth - dp16,
            dp16,
            dp4,
            dp4,
            paint
        )

        canvas.translate(0f, dp32)
        totalTranslateY += dp32

        if (totalTranslateY >= measuredHeight) {
            return
        }

        //绘制第7根线
        canvas.drawRoundRect(
            dp16,
            0f,//距离上面16dp
            measuredWidth - dp84,
            dp16,
            dp4,
            dp4,
            paint
        )

        canvas.translate(0f, dp56)
        totalTranslateY += dp56

        if (totalTranslateY >= measuredHeight) {
            return
        }

        //绘制第8根线
        canvas.drawRoundRect(
            dp16,
            0f,//距离上面16dp
            measuredWidth - dp16,
            dp16,
            dp4,
            dp4,
            paint
        )


        canvas.translate(0f, dp32)
        totalTranslateY += dp32

        if (totalTranslateY >= measuredHeight) {
            return
        }

        //绘制第9根线
        canvas.drawRoundRect(
            dp16,
            0f,//距离上面16dp
            measuredWidth - dp16,
            dp16,
            dp4,
            dp4,
            paint
        )

        canvas.translate(0f, dp32)
        totalTranslateY += dp32

        if (totalTranslateY >= measuredHeight) {
            return
        }

        //绘制第10根线
        canvas.drawRoundRect(
            dp16,
            0f,//距离上面16dp
            measuredWidth - dp84,
            dp16,
            dp4,
            dp4,
            paint
        )

        //canvas.translate(-startMargin, -topMargin - dp16)


//        if (topRectWidth > 0) {
//            canvas.translate(startMargin.toFloat(), dp16.toFloat())
//            rectF.set(0f, 0f, topRectWidth.toFloat(), topRectWidth.toFloat())
//
//            firstLineRectF.set(0f, 0f, topRectWidth / 2f, topFirstLineHeight)
//
//            secondLineRectF.set(0f, 0f, topRectWidth / 3f, topSecondLineHeight)
//
//            thirdLineRectF.set(0f, 0f, topThirdLineWidth, topThirdLineHeight)
//
//            for (j in 0 until 2) {
//
//                for (i in 0 until 3) {
//                    canvas.drawRoundRect(rectF, rectRadius, rectRadius, paint)
//
//                    canvas.translate(0f, topRectWidth + dp16.toFloat())
//                    canvas.drawRoundRect(firstLineRectF, rectRadius, rectRadius, paint)
//
//                    canvas.translate(0f, topFirstLineHeight + dp16.toFloat())
//                    canvas.drawRoundRect(secondLineRectF, rectRadius, rectRadius, paint)
//
//                    canvas.translate(
//                        dp16.toFloat() + topRectWidth,
//                        -topRectWidth - dp16.toFloat() - topFirstLineHeight - dp16.toFloat()
//                    )
//
//                }
//
//                paint.color = Color.GRAY
//
//                canvas.translate(
//                    -3 * (dp16.toFloat() + topRectWidth),
//                    topRectWidth + dp16.toFloat() + topFirstLineHeight + topSecondLineHeight + dp16.toFloat() + dp16.toFloat()
//                )
//
//            }
//
//            canvas.translate(0f, dp20)
//
//            //绘制中间那条短线。
//            canvas.translate((measuredWidth - topThirdLineWidth) / 2 - startMargin, 0f)
//            canvas.drawRoundRect(thirdLineRectF, rectRadius, rectRadius, paint)
//
//            canvas.translate(-((measuredWidth - topThirdLineWidth) / 2 - startMargin), dp20 * 3)
//
//            //绘制横向的布局
//
//            for (i in 0 until 2) {
//                canvas.drawRoundRect(forthLineRectF, rectRadius, rectRadius, paint)
//                canvas.translate(0f, dp20 * 3)
//
//                canvas.drawRoundRect(bottomRectF, rectRadius, rectRadius, paint)
//
//                //绘制右边的三条线
//
//                canvas.translate(bottomRectF.width() + dp16, 0f)
//
//                canvas.drawRoundRect(fifthLineRectF, rectRadius, rectRadius, paint)
//
//
//                canvas.translate(0f, fifthLineRectF.height() + dp16)
//
//                canvas.drawRoundRect(sixthLineRectF, rectRadius, rectRadius, paint)
//
//                canvas.translate(
//                    0f,
//                    bottomRectF.height() - seventhLineRectF.height() - fifthLineRectF.height() - dp16
//                )
//
//                canvas.drawRoundRect(seventhLineRectF, rectRadius, rectRadius, paint)
//
//                canvas.translate(-(bottomRectF.width() + dp16), bottomRectF.height() + dp20)
//            }
//
//
//        }
    }


}