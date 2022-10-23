package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
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

    //注释1处，记录两个View 的下标。
    var mFirstViewIndex = -1
    var mSecondViewIndex = -1
    var mFirstView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

            mFirstViewIndex = indexOfChild(mFirstView)

        }
    var mSecondView: View? = null
        set(value) {
            field = value
            addView(
                value,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

            mSecondViewIndex = indexOfChild(mSecondView)
        }

    init {

        //注释2处，这里调用的是 ViewGroup 的 setChildrenDrawingOrderEnabled 方法 并传递了 true。
        isChildrenDrawingOrderEnabled = true

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

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        //注释3处，修改绘制顺序，当要绘制 mFirstView的时候，绘制mSecondView。要绘制 mSecondView 的时候，绘制mFirstView。
        if (i == mFirstViewIndex) {
            return mSecondViewIndex
        } else if (i == mSecondViewIndex) {
            return mFirstViewIndex
        }
        return super.getChildDrawingOrder(childCount, i)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }
}