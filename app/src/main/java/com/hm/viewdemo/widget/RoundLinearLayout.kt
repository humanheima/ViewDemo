package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout

/**
 * Created by p_dmweidu on 2024/9/4
 * Desc: 圆角的线性布局，并剪裁child
 */
class RoundLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var radius: Float = 0f
        set(value) {
            field = value
            clipToOutline = true
            outlineProvider = object : ViewOutlineProvider() {

                override fun getOutline(view: View?, outline: Outline?) {
                    if (view != null && outline != null) {
                        outline.setRoundRect(0, 0, view.width, view.height, value)
                    }
                }
            }
        }

}