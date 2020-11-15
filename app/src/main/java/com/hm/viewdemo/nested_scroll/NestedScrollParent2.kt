package com.hm.viewdemo.nested_scroll

import android.support.v4.view.NestedScrollingChild2

/**
 * Created by dumingwei on 2020/11/15.
 *
 * Desc:
 */
class NestedScrollParent2:NestedScrollingChild2 {
    override fun dispatchNestedScroll(p0: Int, p1: Int, p2: Int, p3: Int, p4: IntArray?, p5: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun dispatchNestedScroll(p0: Int, p1: Int, p2: Int, p3: Int, p4: IntArray?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isNestedScrollingEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun dispatchNestedPreScroll(p0: Int, p1: Int, p2: IntArray?, p3: IntArray?, p4: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun dispatchNestedPreScroll(p0: Int, p1: Int, p2: IntArray?, p3: IntArray?): Boolean {
        TODO("Not yet implemented")
    }

    override fun stopNestedScroll(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun stopNestedScroll() {
        TODO("Not yet implemented")
    }

    override fun hasNestedScrollingParent(p0: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasNestedScrollingParent(): Boolean {
        TODO("Not yet implemented")
    }

    override fun dispatchNestedPreFling(p0: Float, p1: Float): Boolean {
        TODO("Not yet implemented")
    }

    override fun setNestedScrollingEnabled(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun dispatchNestedFling(p0: Float, p1: Float, p2: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun startNestedScroll(p0: Int, p1: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun startNestedScroll(p0: Int): Boolean {
        TODO("Not yet implemented")
    }
}