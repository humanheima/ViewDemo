package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_scroller.*

/**
 * Created by dumingwei on 2020/4/13
 *
 * Desc:
 */
class ScrollerActivity : AppCompatActivity() {


    private val TAG: String = "ScrollerActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ScrollerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroller)
        btnStartScroll.setOnClickListener {
            //向右下方向滚动100像素
            //smoothScrollView.smoothScrollTo(-100, -100)
            smoothScrollView.smoothScrollTo(100, 100)
        }

        btnScroll.setOnClickListener {
            Log.i(TAG, "onCreate: ${smoothScrollView.scrollY}")
        }
    }


}
