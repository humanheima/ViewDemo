package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.MessageQueue
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityHandlerBinding

/**
 * Crete by dumingwei on 2020-01-08
 * Desc:测试使用Handler监听卡顿
 *
 */
class HandlerActivity : AppCompatActivity() {


    private val TAG = "HandlerActivity"

    private lateinit var handler: Handler


    private lateinit var binding: ActivityHandlerBinding

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, HandlerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHandlerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //添加Printer
        /*Looper.getMainLooper().setMessageLogging { str ->
            Log.i(TAG, "onCreate: Printer $str")
        }*/

        binding.btnSendMessage.setOnClickListener {
            handler.post {
                prepare()
            }

        }

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.i(TAG, "handleMessage: ")
                handler.sendEmptyMessageDelayed(1, 200)
            }
        }
    }

    private fun prepare() {
        for (i in 0..10000) {
            Log.i(TAG, "prepare: $i")
        }
        Thread.sleep(1000)
        prepareFirst()
        prepareSecond()
        prepareFirst()
        prepareThird()
    }


    private fun prepareFirst() {
        Log.i(TAG, "prepareFirst: ")
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
        Log.i(TAG, "onCreate: send msg")
    }

    private fun testIdleHandler() {
        val idleHandler = object : MessageQueue.IdleHandler {
            override fun queueIdle(): Boolean {
                Log.i(TAG, "queueIdle: ")
                //return false会自动移除
                //注意也不要做太耗时的操作
                return false
            }
        }
        Looper.myQueue().addIdleHandler(idleHandler)
        //或者调用removeIdleHandler 主动移除。
        //Looper.myQueue().removeIdleHandler(idleHandler)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}
