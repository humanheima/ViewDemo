package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityViewDragHelperBinding

class ViewDragHelperActivity : BaseActivity<ActivityViewDragHelperBinding>() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ViewDragHelperActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding() = ActivityViewDragHelperBinding.inflate(layoutInflater)

    override fun initData() {

    }

    override fun bindEvent() {}

}
