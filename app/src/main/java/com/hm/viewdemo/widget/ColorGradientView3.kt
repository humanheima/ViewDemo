package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2024/1/24
 * Desc: 渐变背景View
 * 传入颜色，和渐变方向
 */
class ColorGradientView3 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ColorGradientView3"
    }

    /**
     * 渐变方向，默认从上到下
     */
    private var orientation = GradientDrawable.Orientation.TOP_BOTTOM

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorGradientView3)
        //这里默认颜色，要用 getResources().getColor()，不能直接用 R.color.color_00f1f1
        val startColor = ta.getColor(R.styleable.ColorGradientView3_start_color, 0)
        val centerColor = ta.getColor(R.styleable.ColorGradientView3_center_color, 0)
        val endColor = ta.getColor(R.styleable.ColorGradientView3_end_color, 0)
        orientation = GradientDrawable.Orientation.values().get(
            ta.getInt(
                R.styleable.ColorGradientView3_gradient_orientation,
                GradientDrawable.Orientation.TOP_BOTTOM.ordinal
            )
        )
        ta.recycle()
        Log.i(
            TAG,
            "init: startColor=" + startColor + ",centerColor=" + centerColor + ",endColor=" + endColor
                    + ",orientation=" + orientation
        )
        val colors: MutableList<Int> = ArrayList(3)
        if (startColor != 0) {
            colors.add(startColor)
        }
        if (centerColor != 0) {
            colors.add(centerColor)
        }
        if (endColor != 0) {
            colors.add(endColor)
        }
        val size = colors.size
        if (size == 0) {
            background = null
            return
        }
        val colorArray: IntArray = colors.toIntArray()
        background = GradientDrawable(orientation, colorArray)
    }

}