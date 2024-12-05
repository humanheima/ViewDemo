package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.SizeF
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2024/12/5
 * Desc: 拷贝 CutCornerImageView 的逻辑
 */
class CutCornerRelativeLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val TAG = "CutCornerRelativeLayout"

    private var cornerRadius: Float = 0f
    private var cutRadius: Float = 0f
    private var path = Path()
    private var buttonSize = SizeF(0f, 0f)
    private var paint: Paint = Paint().apply {
        isAntiAlias = true
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CutCornerRelativeLayout)
        cornerRadius = typedArray.getDimension(R.styleable.CutCornerImageView_corner_radius, 0f)
        cutRadius = typedArray.getDimension(R.styleable.CutCornerImageView_cut_radius, 0f)
        val width = typedArray.getDimension(R.styleable.CutCornerImageView_cut_width, 0f)
        val height =
            typedArray.getDimension(R.styleable.CutCornerImageView_cut_height, 0f)
        buttonSize = SizeF(width, height)
        typedArray.recycle()

        ColorUtils.setAlphaComponent(Color.BLACK, 0)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        path.moveTo(0f, cornerRadius)
        //左上角圆角
        path.quadTo(0f, 0f, cornerRadius, 0f)
        path.lineTo((w - cornerRadius).toFloat(), 0f)
        //右上角圆角
        path.quadTo(w.toFloat(), 0f, w.toFloat(), cornerRadius)

        //右边
        path.lineTo(w.toFloat(), h - cornerRadius - buttonSize.height)
        //右下角矩形
        path.quadTo(
            w.toFloat(),
            h.toFloat() - buttonSize.height,
            w - cornerRadius,
            h.toFloat() - buttonSize.height
        )
        // 按钮上边
        path.lineTo(w - buttonSize.width + cutRadius, h - buttonSize.height)

        // 按钮左上角圆角
        path.quadTo(
            w - buttonSize.width,
            h - buttonSize.height,
            w - buttonSize.width,
            h - buttonSize.height + cutRadius
        )
        //按钮左边
        path.lineTo(w - buttonSize.width, h - cornerRadius)

        //按钮左下角圆角
        path.quadTo(
            w - buttonSize.width,
            h.toFloat(),
            w - buttonSize.width - cornerRadius,
            h.toFloat()
        )
        path.lineTo(cornerRadius, h.toFloat())

        //左下角矩形
        path.quadTo(0f, h.toFloat(), 0f, h - cornerRadius)
        path.close()
        Log.d(TAG, "onSizeChanged: path = $path")
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
    }

}