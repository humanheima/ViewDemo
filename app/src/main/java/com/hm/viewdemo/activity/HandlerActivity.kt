package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_handler.*

/**
 * Crete by dumingwei on 2020-01-08
 * Desc:测试使用Handler监听卡顿
 *
 */
class HandlerActivity : AppCompatActivity() {


    private val TAG = "HandlerActivity"

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, HandlerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)

        btnSendMessage.setOnClickListener {
            handler.post {
                Thread.sleep(2000)
                Log.d(TAG, "onCreate: send msg")
            }

        }
    }
}
