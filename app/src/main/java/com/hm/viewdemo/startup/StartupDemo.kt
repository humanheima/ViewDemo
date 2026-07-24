package com.hm.viewdemo.startup

import android.content.Context
import android.view.Choreographer
import androidx.startup.AppInitializer
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

object StartupDemo {

    private val deferredInitStarted = AtomicBoolean(false)
    private val deferredExecutor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "startup-deferred")
    }

    fun triggerDeferredInit(context: Context) {
        if (!deferredInitStarted.compareAndSet(false, true)) {
            return
        }
        val appContext = context.applicationContext
        Choreographer.getInstance().postFrameCallback {
            StartupTrace.log("First frame rendered")
            deferredExecutor.execute {
                StartupTrace.log("Deferred init start")
                AppInitializer.getInstance(appContext)
                    .initializeComponent(DeferredAnalyticsInitializer::class.java)
                StartupTrace.log("Deferred init end")
            }
        }
    }
}
