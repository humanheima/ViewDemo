package com.hm.viewdemo.extension

import android.content.Context
import android.view.MotionEvent
import com.hm.viewdemo.util.ScreenUtil

fun String?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}

fun getActionName(action: MotionEvent): String {
    return when (action.action) {
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
        MotionEvent.ACTION_UP -> "ACTION_UP"
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
        MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
        else -> "Other"
    }
}

fun Int.dp2px(context: Context): Int {
    return ScreenUtil.dpToPx(context, this)
}

fun Int.dp2pxFloat(context: Context): Float {
    return ScreenUtil.dpToPxFloat(context, this)
}

