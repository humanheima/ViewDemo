package com.hm.viewdemo.startup

import android.content.Context
import androidx.startup.Initializer

class ProcessGuardInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        StartupTrace.log("ProcessGuardInitializer.create")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
