package com.hm.viewdemo

import android.app.Application
import android.os.Debug

/**
 * Created by dumingwei on 2020-01-20.
 * Desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Debug.startMethodTracing()
    }
}