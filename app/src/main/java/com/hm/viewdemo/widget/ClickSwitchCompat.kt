package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.SwitchCompat

/**
 * Created by p_dmweidu on 2023/8/28
 * Desc: 自定义SwitchCompat，适用场景
 * 1. 点击ClickSwitchCompat，请求网络，网络请求成功后，改变SwitchCompat状态。如果请求失败，不改变SwitchCompat状态
 */
class ClickSwitchCompat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwitchCompat(context, attrs) {

    private var specialClickListener: OnClickListener? = null
    override fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        //super.setOnCheckedChangeListener(listener)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        //super.setOnClickListener(l)
        //specialClickListener = l
    }

    fun setSpecialClickListener(l: OnClickListener?) {
        specialClickListener = l
    }

    fun getSpecialClickListener(): OnClickListener? {
        return specialClickListener
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            specialClickListener?.onClick(this)
        }
        return true

    }

}