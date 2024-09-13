package com.hm.viewdemo.widget.xfermode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil

/**
 * 渐隐遮罩层
 * 1.作为容器使用，与子View大小保持一致
 * 2. 顶部是透明区域
 * 3. 透明区域下面是渐变区域
 *
 * 参考链接：https://blog.csdn.net/leilifengxingmw/article/details/105351758
 * 先绘制的是目标图像DST，后绘制的是源图像SRC。
 * 在我们这个例子中，目标图像DST是ViewGroup的内容，源图像SRC是上面的蒙层。
 *
 * CLEAR模式：  alpha_out = 0
 *             C_out     = 0
 *
 * SRC模式：  alpha_out = alpha_src
 *           C_out     = C_src
 *
 * DST_OUT模式：  alpha_out = (1 - alpha_src) * alpha_dst
 *               C_out     = (1 - alpha_src) * C_dst
 *
 */
class SimpleMaskViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    companion object {
        private const val TAG = "SpecialMaskViewGroup"

    }

    // 透明画笔
    private var mTransparentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
    }

    // 渐变画笔
    private var mGradientPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mGradientColors by lazy {
        intArrayOf(0xffffffff.toInt(), 0x00000000)
    }
    private var transparentSize = ScreenUtil.dpToPx(getContext(), 0).toFloat()
    private var gradientSize = ScreenUtil.dpToPx(getContext(), 0).toFloat()
    private var enabled = true
    private var mTransparentRectF = RectF()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SpecialMaskViewGroup)
        transparentSize =
            a.getDimension(R.styleable.SpecialMaskViewGroup_transparent_size, transparentSize)
        a.recycle()
    }

    fun setGradientEnabled(enabled: Boolean) {
        if (this.enabled == enabled) {
            return
        }
        this.enabled = enabled
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            sizeChanged()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (enabled.not()) {
            super.dispatchDraw(canvas)
            return
        }
        val layerSave = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        super.dispatchDraw(canvas)

        //mTransparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        mTransparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        // 绘制透明蒙层
        canvas.drawRect(mTransparentRectF, mTransparentPaint)
        canvas.restoreToCount(layerSave)
    }

    private fun sizeChanged() {
        mTransparentRectF = RectF(0f, 0f, width.toFloat(), transparentSize)

    }

    fun setTransparentSize(transparentSize: Int) {
        this.transparentSize = transparentSize.toFloat()
        sizeChanged()
        invalidate()
    }

}