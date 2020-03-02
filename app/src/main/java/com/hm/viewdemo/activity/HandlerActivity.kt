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

    private lateinit var handler: Handler


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, HandlerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)


        /*Looper.myQueue().addIdleHandler {
            Log.d(TAG, "onCreate: " + Thread.currentThread().name)
            true
        }*/

        //添加Printer
        /*Looper.getMainLooper().setMessageLogging { str ->
            Log.d(TAG, "onCreate: Printer $str")
        }*/

        btnSendMessage.setOnClickListener {
            handler.post {
                prepare()
            }

        }

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(TAG, "handleMessage: ")
                handler.sendEmptyMessageDelayed(1, 200)
            }
        }
    }

    private fun prepare() {
        for (i in 0..10000) {
            Log.d(TAG, "prepare: $i")
        }
        Thread.sleep(1000)
        prepareFirst()
        prepareSecond()
        prepareFirst()
        prepareThird()
    }


    private fun prepareFirst() {
        Log.d(TAG, "prepareFirst: ")
        Thread.sleep(1500)
    }

    private fun prepareSecond() {
        Thread.sleep(2000)
    }

    private fun prepareThird() {
        Thread.sleep(4000)
    }

    private fun longDurationMethod() {
        Thread.sleep(2000)
        Log.d(TAG, "onCreate: send msg")
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}
