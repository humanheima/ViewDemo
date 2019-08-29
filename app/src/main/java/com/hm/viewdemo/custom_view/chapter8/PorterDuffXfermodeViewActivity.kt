package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-29
 * Desc:
 *
 */

class PorterDuffXfermodeViewActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PorterDuffXfermodeViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_porter_duff_xfermode_view)
    }


}
