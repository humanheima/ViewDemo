package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2022/11/6
 * Desc:测试如下场景
 * 1. ScrollView不设置背景
 * 2. ScrollView的直接子View设置圆角
 * 3. 在滑动的过程中，如果保持圆角
 */
class RoundScrollViewActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, RoundScrollViewActivity::class.java)
            context.startActivity(starter)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_scroll_view)
    }
}