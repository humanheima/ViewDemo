package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min
import kotlin.math.sin

/**
 * 水波进度球：一个圆形容器，内部的水面随进度上涨，水面上有持续流动的正弦波。
 *
 * 画法（圆形裁剪用 PorterDuff 实现，避免 clipPath 抗锯齿差）：
 *   1. 先把两层正弦波 + 水面下的填充画到离屏图层；
 *   2. 用 DST_IN 叠一个实心圆，只保留圆内的部分 → 得到“球里的水”；
 *   3. 圆环描边 + 中间百分比文字。
 *
 * 波形：y = amplitude * sin(2π/λ * x + phase) + waterLine
 *   phase 随时间平移 → 水在流动；两层波相位/速度略不同 → 更自然。
 */
class WaveProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    /** 进度 0~100，决定水面高度。越大水越满 */
    var progress: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 100f)
            invalidate()
        }

    /** 波的振幅占半径的比例，越大波越高 */
    var amplitudeRatio = 0.08f
        set(value) {
            // 限制振幅不宜过大，否则水面会“飞出”圆环，破坏美观。0.3 已经很夸张了。
            field = value.coerceIn(0f, 0.3f)
            invalidate()
        }

    /** 一个圆直径范围内有几个完整波长，越大波越密 */
    var waveCount = 1.6f
        set(value) {
            field = value.coerceAtLeast(0.1f)
            invalidate()
        }

    var waveColor = Color.parseColor("#4FC3F7")
        set(value) {
            field = value
            invalidate()
        }

    var ringColor = Color.parseColor("#0288D1")
        set(value) {
            field = value
            invalidate()
        }

    var ringWidth = dp(3f)
        set(value) {
            field = value
            invalidate()
        }

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.parseColor("#0277BD")
    }

    private val dstIn = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private val wavePath = Path()
    private val ringRect = RectF()

    // 两层波各自的相位，靠动画持续推进
    private var phase1 = 0f
    private var phase2 = 0f

    private var animator: android.animation.ValueAnimator? = null

    /** 开始水流动画（相位无限平移） */
    fun startAnimation() {
        if (animator?.isRunning == true) return
        animator = android.animation.ValueAnimator.ofFloat(0f, (2 * Math.PI).toFloat()).apply {
            duration = 1500
            interpolator = LinearInterpolator()
            repeatCount = android.animation.ValueAnimator.INFINITE
            addUpdateListener {
                val base = it.animatedValue as Float
                phase1 = base
                // 必须用整数倍：动画每轮回绕到 2π，整数倍仍是 2π 整数倍 ≡ 0，
                // 回绕处 sin 连续。非整数倍（如 1.4）会在 2.8π 处跳变 → 波“突然重来”。
                // -2 = 反向、双倍速，与第一层错开又保持连续。
                phase2 = base * -2f
                invalidate()
            }
            start()
        }
    }

    fun stopAnimation() {
        animator?.cancel()
        animator = null
    }

    /** 水面是否正在流动 */
    fun isFlowing(): Boolean = animator?.isRunning == true

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        //直径
        val diameter = min(w, h) - ringWidth
        if (diameter <= 0) return

        val cx = w / 2f
        val cy = h / 2f
        //半径
        val radius = diameter / 2f
        // 振幅 = 半径 * 比例
        val amp = radius * 2f * amplitudeRatio
        // 进度 0 时水面在圆底部，100 时在顶部
        val waterLine = cy + radius - (progress / 100f) * diameter
        // 波长 = 直径 / 波数，对应一个2π周期
        val waveLen = diameter / waveCount

        // 用离屏图层，方便后面 DST_IN 裁成圆
        val layer = canvas.saveLayer(0f, 0f, w, h, null)

        wavePaint.color = waveColor
        // 第一层波：实心
        wavePaint.alpha = 255
        drawWave(canvas, cx - radius, cx + radius, cy + radius, waterLine, amp, waveLen, phase1)
        // 第二层波：半透明，制造层叠感
        wavePaint.alpha = 120
        //drawWave(canvas, cx - radius, cx + radius, cy + radius, waterLine, amp * 0.8f, waveLen, phase2)

        // DST_IN：只保留落在圆内的像素
        circlePaint.color = Color.BLACK
        circlePaint.xfermode = dstIn
        canvas.drawCircle(cx, cy, radius, circlePaint)
        circlePaint.xfermode = null

        canvas.restoreToCount(layer)

        // 圆环边框
        ringPaint.color = ringColor
        ringPaint.strokeWidth = ringWidth
        ringRect.set(cx - radius, cy - radius, cx + radius, cy + radius)
        canvas.drawArc(ringRect, 0f, 360f, false, ringPaint)

        // 中间百分比文字
        textPaint.textSize = radius * 0.5f
        val text = "${progress.toInt()}%"
        val fm = textPaint.fontMetrics
        val baseline = cy - (fm.ascent + fm.descent) / 2f
        canvas.drawText(text, cx, baseline, textPaint)
    }

    /**
     * 画一条正弦波下方的填充区域：从左到右沿正弦曲线走，再沿底边闭合。
     * @param left 波的起点 x 坐标（圆左边界）
     * @param right 波的终点 x 坐标（圆右边界）
     * @param bottom 波的下边界 y 坐标（圆底边界
     * @param waterLine 水面高度 y 坐标
     * @param amp 波的振幅
     * @param waveLen 波长
     * @param phase 波的相位（水平平移）
     */
    private fun drawWave(
        canvas: Canvas, left: Float, right: Float, bottom: Float,
        waterLine: Float, amp: Float, waveLen: Float, phase: Float
    ) {
        wavePath.reset()
        wavePath.moveTo(left, bottom)
        val k = (2 * Math.PI / waveLen).toFloat()
        var x = left
        while (x <= right) {
            // y = amp * sin(k*x + phase) + waterLine
            //   sin 输出永远是 [-1,1]，必须乘振幅 amp 才能放大到屏幕尺寸；
            //   若去掉 amp，波只在水面上下摆动 ±1px，肉眼几乎是直线，波浪消失。
            //   想要更平缓的波应调小 amplitudeRatio，而不是删 amp。
            val y = (amp * sin(k * (x - left) + phase)) + waterLine
            wavePath.lineTo(x, y)
            x += 2f          // 步长 2px，平滑且不费
        }
        wavePath.lineTo(right, bottom)
        wavePath.close()
        canvas.drawPath(wavePath, wavePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    private fun dp(value: Float): Float = value * resources.displayMetrics.density
}
