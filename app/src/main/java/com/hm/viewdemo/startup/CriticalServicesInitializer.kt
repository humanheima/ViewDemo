package com.hm.viewdemo.startup

import android.content.Context
import androidx.startup.Initializer

/**
 * Startup task that is required before UI becomes interactive.
 */
class CriticalServicesInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        StartupTrace.log("CriticalServicesInitializer.create")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            AppConfigInitializer::class.java,
            StartupLoggerInitializer::class.java,
        )
    }
}
