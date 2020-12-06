package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2020/12/2
 *
 * Desc: 吸边View的测试
 */
class SorbViewTestActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, SorbViewTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absorb_view_test)
    }
}