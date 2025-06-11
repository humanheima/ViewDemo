package com.hm.viewdemo.guide

import android.content.Context

abstract class BaseGuideStrategy(context: Context) : GuideStrategy {
    protected val context = context.applicationContext ?: context

}