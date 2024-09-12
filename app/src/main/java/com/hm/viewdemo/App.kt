package com.hm.viewdemo

import android.app.Application
import android.os.Debug

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
        Debug.startMethodTracing()
    }
}