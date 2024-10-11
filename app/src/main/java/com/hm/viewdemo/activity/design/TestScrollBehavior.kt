package com.hm.viewdemo.activity.design

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class TestScrollBehavior : AppBarLayout.Behavior() {

    private val TAG = "TestScrollBehavior"

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        dependency: View
    ): Boolean {
        Log.d(TAG, "layoutDependsOn: dependency = $dependency")
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        //Log.d(TAG, "onTouchEvent: ev = $ev")
        return super.onTouchEvent(parent, child, ev)
    }
}