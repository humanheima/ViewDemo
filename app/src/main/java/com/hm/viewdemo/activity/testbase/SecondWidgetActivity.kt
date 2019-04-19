package com.hm.viewdemo.activity.testbase

import android.content.Context
import android.content.Intent
import com.hm.viewdemo.R

class SecondWidgetActivity : BaseWidgetActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, SecondWidgetActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_second_widget
    }
}
