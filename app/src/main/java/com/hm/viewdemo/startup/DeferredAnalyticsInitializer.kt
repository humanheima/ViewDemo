package com.hm.viewdemo.startup

import android.content.Context
import androidx.startup.Initializer

/**
 * Simulates a non-critical SDK init that should not block cold start.
 */
class DeferredAnalyticsInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Thread.sleep(350)
        StartupTrace.log("DeferredAnalyticsInitializer.create")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(CriticalServicesInitializer::class.java)
    }
}
