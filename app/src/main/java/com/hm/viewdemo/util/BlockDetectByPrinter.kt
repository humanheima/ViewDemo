package com.hm.viewdemo.util

import android.os.Looper
import android.util.Printer

/**
 * Created by dumingwei on 2020-01-08.
 * Desc:
 */
class BlockDetectByPrinter {

    companion object {

        private val START = ">>>>> Dispatching"
        private val END = "<<<<< Finished"

        @JvmStatic
        fun start() {
            Looper.getMainLooper().setMessageLogging(object : Printer {
                override fun println(x: String?) {
                    if (x?.startsWith(START) == true) {
                        LogMonitor.getInstance().startMonitor()
                    }
                    if (x?.startsWith(END) == true) {
                        LogMonitor.getInstance().removeMonitor()
                    }
                }
            })

        }
    }
}