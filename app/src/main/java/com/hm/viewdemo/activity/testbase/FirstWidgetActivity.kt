package com.hm.viewdemo.activity.testbase

import android.content.Context
import android.content.Intent
import com.hm.viewdemo.R

class FirstWidgetActivity : BaseWidgetActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, FirstWidgetActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_first_widget

}
