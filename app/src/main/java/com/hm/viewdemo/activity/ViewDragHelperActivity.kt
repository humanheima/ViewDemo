package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent

import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity

class ViewDragHelperActivity : BaseActivity() {

    override fun bindLayout(): Int {
        return R.layout.activity_view_drag_helper
    }

    override fun initData() {

    }

    override fun bindEvent() {}

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ViewDragHelperActivity::class.java)
            context.startActivity(starter)
        }
    }
}
