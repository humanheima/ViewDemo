package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ScrollView
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2022/11/6
 * Desc:
 */
class RoundScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    var radius: Float = 0f
    private val TAG = "RoundScrollView"

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundScrollView)
        radius = ta.getDimension(
            R.styleable.RoundScrollView_radius,
            0f
        )
        ta.recycle()
        Log.i(TAG, "radius = $radius")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clipToOutline = true
            outlineProvider = object : ViewOutlineProvider() {

                override fun getOutline(view: View?, outline: Outline?) {
                    if (view != null && outline != null) {
                        outline.setRoundRect(0,0,view.width,view.height,radius)
                    }
                }
            }
        }
    }


}