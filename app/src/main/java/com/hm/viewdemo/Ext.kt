package com.hm.viewdemo

import android.content.Context
import android.util.TypedValue


fun Int.dp2px(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), context.resources.displayMetrics
    ).toInt()
}

fun Int.sp2px(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), context.resources.displayMetrics
    ).toInt()
}

fun Float.dp2px(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, context.resources.displayMetrics
    )
}

fun Float.sp2px(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this, context.resources.displayMetrics
    )
}

fun Double.degress2radians(): Double {
    return this * (Math.PI / 180)
}

fun Double.radians2degress(): Double {
    return this * (180 / Math.PI)
}