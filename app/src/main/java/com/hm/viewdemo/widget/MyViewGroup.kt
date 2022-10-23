package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by p_dmweidu on 2022/10/23
 * Desc:
 */
class MyViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    var mFirstView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )
        }
    var mSecondView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

        }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(mFirstView, widthMeasureSpec, heightMeasureSpec)
        measureChild(mSecondView, widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mFirstView?.layout(l, t, r, b)
        mSecondView?.layout(l, t, r, b)
    }

}