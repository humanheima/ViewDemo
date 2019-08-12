package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-12
 * Desc: 学习Path的基本操作
 * 参考: https://www.gcssloop.com/customview/Path_Basic/
 *
 */
class DrawPathActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, DrawPathActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_path)
    }
}
