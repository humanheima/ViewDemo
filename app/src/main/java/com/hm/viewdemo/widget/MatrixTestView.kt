package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2023/8/10
 * Desc: 测试矩阵，基础方法使用
 * 参考链接：https://juejin.cn/post/6844903507577815053
 */
class MatrixTestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var testMatrix = Matrix()

    private var bmpDst: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_dog, null)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bmpDst, testMatrix, null)
        //平移，向右平移200，向下平移300
        //testMatrix.setTranslate(200f, 300f)

        //放大，x轴放大2倍，y轴放大2倍
        //testMatrix.setScale(2f, 2f)


        //旋转，以图片中心点为中心点，旋转90度
        //testMatrix.setRotate(90f, bmpDst.width / 2f, bmpDst.height / 2f)

        /**
         * setSinCos，设置旋转的另一种方式
         *
         * Set the matrix to rotate by the specified sine and cosine values, with a pivot point at (px,
         * py). The pivot point is the coordinate that should remain unchanged by the specified transformation.
         * x = x0*cos(a) -  y0*sin(a)
         * y = x0*sin(a) +  y0*cos(a)
         * sin(0) = 0，sin(30) = 1/2 ，sin(90) = 1
         * cos(0) = 1，cos(60) = 1/2 ，sin(90) = 0
         *
         */
        //旋转90度
        //testMatrix.setSinCos(1.0f, 0f, bmpDst.width / 2f, bmpDst.height / 2f)

        //Note:  setSkew 等待研究
        //testMatrix.setSkew(0.2f, 0f, bmpDst.width / 2f, bmpDst.height / 2f)
        //testMatrix.setSkew(-0.8f, 0f, bmpDst.width / 2f, bmpDst.height / 2f)

        testMatrix.setTranslate(200f, 300f)
        //矩阵前乘
        testMatrix.preScale(0.5f, 0.5f)

        //矩阵后乘
        //testMatrix.postScale(0.5f, 0.5f)

        canvas.drawBitmap(bmpDst, testMatrix, null)


    }

}