package com.hm.viewdemo.startup

import android.content.Context
import androidx.startup.Initializer

class AppConfigInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        StartupTrace.log("AppConfigInitializer.create")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(ProcessGuardInitializer::class.java)
    }
}
