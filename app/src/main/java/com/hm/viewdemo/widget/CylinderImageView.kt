package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.cos
import kotlin.math.sin

/**
 * 把图片贴在一个圆柱的正面，呈现“向屏幕内弯曲”的效果（像看到圆柱体的前半部分）。
 *
 * 原理：drawBitmapMesh —— 把图片切成 meshW × meshH 的网格，对每个顶点做
 * “圆柱投影 + 透视”得到新坐标，再让系统按新网格重新贴图。整张图连续形变，没有接缝。
 *
 * 几何（以竖直轴的圆柱为例）：
 *   r     = (W/2) / sin(arc/2)        让图片边缘正好落在 ±W/2
 *   θ     = (u - 0.5) * arc           某列相对中线的角度，u∈[0,1]
 *   depth = r * (1 - cosθ)            该列陷进屏幕的深度，中线=0，边缘最大
 *   persp = cam / (cam + depth)       透视缩放，越靠边越小
 *   x'    = cx + persp * r * sinθ     横向：sin 投影 + 透视收缩
 *   y'    = cy + persp * (v-0.5) * H  纵向：离中心越远越往里收 → 上下边缘弯曲
 */
class CylinderImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    // 网格密度：越大越平滑。顶点数 = (meshW+1) * (meshH+1)，够用即可，别太离谱
    private val meshW = 40
    private val meshH = 40
    private val verts = FloatArray((meshW + 1) * (meshH + 1) * 2)

    /** 图片包裹圆柱的总角度（度）。0 = 不弯，越大弯得越狠。建议 0~150 */
    var arcDegrees = 120f
        set(value) {
            field = value.coerceIn(0f, 179f)
            invalidate()
        }

    /** 视点到圆柱正面的距离系数，越小透视越强（边缘收缩越明显） */
    var cameraFactor = 1.2f
        set(value) {
            field = value.coerceAtLeast(0.1f)
            invalidate()
        }

    /** true = 绕水平轴弯曲（上下边陷入屏幕）；false = 绕竖直轴弯曲（左右边陷入屏幕） */
    var horizontalAxis = false
        set(value) {
            field = value
            invalidate()
        }

    // 把 drawable 栅格化后的缓存，避免在 onDraw 里反复创建
    private var cache: Bitmap? = null
    private var cacheKey: Drawable? = null
    private var cacheW = 0
    private var cacheH = 0

    override fun onDraw(canvas: Canvas) {
        // 拿不到内容位图（如尺寸未就绪）才回退系统绘制
        val bmp = obtainBitmap() ?: run {
            super.onDraw(canvas)
            return
        }

        canvas.save()
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        if (arcDegrees <= 0f) {
            // 不弯曲：直接画已按内容区栅格化好的平面位图。
            // 不能用 super.onDraw —— obtainBitmap 改过 drawable 的 bounds，
            // 系统 ImageView 的绘制矩阵会对不上，导致图错位/不可见。
            canvas.drawBitmap(bmp, 0f, 0f, null)
        } else {
            // arc>0 才做圆柱投影（arc=0 会让 r = span/2 / sin(0) 除零）
            buildMesh(bmp.width.toFloat(), bmp.height.toFloat())
            canvas.drawBitmapMesh(bmp, meshW, meshH, verts, 0, null, 0, null)
        }
        canvas.restore()
    }

    private fun buildMesh(w: Float, h: Float) {
        val arc = Math.toRadians(arcDegrees.toDouble())
        val half = arc / 2.0
        val cx = w / 2f
        val cy = h / 2f

        // 沿哪个轴弯曲，就用哪个方向的长度去算半径
        val span = if (horizontalAxis) h else w
        val r = (span / 2f) / sin(half).toFloat()
        val cam = r * cameraFactor

        var idx = 0
        for (i in 0..meshH) {
            val v = i.toFloat() / meshH          // 0..1，纵向比例
            for (j in 0..meshW) {
                val u = j.toFloat() / meshW      // 0..1，横向比例

                // t 是“沿弯曲方向”的比例，s 是“另一方向”的比例
                val t = if (horizontalAxis) v else u
                val s = if (horizontalAxis) u else v

                val theta = ((t - 0.5f) * arc).toFloat()
                val depth = r * (1f - cos(theta.toDouble()).toFloat())
                val persp = cam / (cam + depth)
                val proj = persp * r * sin(theta.toDouble()).toFloat()  // 沿弯曲方向的投影位置

                if (horizontalAxis) {
                    // 绕水平轴：纵向做 sin 投影，横向被透视收缩
                    verts[idx++] = cx + persp * (s - 0.5f) * w
                    verts[idx++] = cy + proj
                } else {
                    // 绕竖直轴：横向做 sin 投影，纵向被透视收缩
                    verts[idx++] = cx + proj
                    verts[idx++] = cy + persp * (s - 0.5f) * h
                }
            }
        }
    }

    /**
     * 取出 drawable 对应的 Bitmap，按 View 内容区尺寸栅格化并缓存。
     *
     * 关键：mesh 的坐标是以这里返回的 bitmap 尺寸为基准算的，所以必须栅格化到
     * 内容区大小，而不能直接返回原图——否则图会以原图像素尺寸绘制在左上角。
     */
    private fun obtainBitmap(): Bitmap? {
        val d = drawable ?: return null
        val cw = width - paddingLeft - paddingRight
        val ch = height - paddingTop - paddingBottom
        if (cw <= 0 || ch <= 0) return null

        val valid = cache?.let { !it.isRecycled } ?: false
        if (valid && cacheKey === d && cacheW == cw && cacheH == ch) {
            return cache
        }

        cache?.recycle()
        val bmp = Bitmap.createBitmap(cw, ch, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)

        // 按 fitCenter 把 drawable 画进内容区：保持比例、居中、尽量铺满
        val iw = d.intrinsicWidth.takeIf { it > 0 } ?: cw
        val ih = d.intrinsicHeight.takeIf { it > 0 } ?: ch
        val scale = minOf(cw / iw.toFloat(), ch / ih.toFloat())
        val dw = (iw * scale).toInt()
        val dh = (ih * scale).toInt()
        val left = (cw - dw) / 2
        val top = (ch - dh) / 2
        d.setBounds(left, top, left + dw, top + dh)
        d.draw(c)

        cache = bmp
        cacheKey = d
        cacheW = cw
        cacheH = ch
        return bmp
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cache?.recycle()
        cache = null
        cacheKey = null
    }
}
