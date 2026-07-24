package com.hm.viewdemo

import android.app.Application
import com.hm.viewdemo.startup.StartupTrace

/**
 * Created by dumingwei on 2020-01-20.
 * Desc:
 */
class App : Application() {


    companion object {

        var instance: App? = null

    }

    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = this
        }
        StartupTrace.log("Application.onCreate")
    }
}
