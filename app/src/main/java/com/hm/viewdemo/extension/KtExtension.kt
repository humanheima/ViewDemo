package com.hm.viewdemo.extension

import android.view.MotionEvent

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