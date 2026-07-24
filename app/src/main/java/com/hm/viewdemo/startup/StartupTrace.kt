package com.hm.viewdemo.startup

import android.os.Build
import android.os.Process
import android.os.SystemClock
import android.util.Log

object StartupTrace {

    private const val TAG = "StartupDemo"

    private val processStartElapsedRealtime: Long by lazy(LazyThreadSafetyMode.NONE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Process.getStartElapsedRealtime()
        } else {
            SystemClock.elapsedRealtime()
        }
    }

    fun log(stage: String) {
        val delta = SystemClock.elapsedRealtime() - processStartElapsedRealtime
        Log.i(TAG, "$stage, +${delta}ms, thread=${Thread.currentThread().name}")
    }
}
