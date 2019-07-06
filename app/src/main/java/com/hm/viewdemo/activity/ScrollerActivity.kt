package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_scroller.*

class ScrollerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroller)
        btnStartScroll.setOnClickListener {
            //向右下方向滚动100像素
            smoothScrollView.smoothScrollTo(-100, -100)
        }

    }

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ScrollerActivity::class.java)
            context.startActivity(intent)
        }
    }
}
