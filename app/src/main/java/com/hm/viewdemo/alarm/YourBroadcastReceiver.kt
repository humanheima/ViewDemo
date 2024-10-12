package com.hm.viewdemo.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


/**
 * Created by p_dmweidu on 2024/10/12
 * Desc:
 */
class YourBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "YourBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        // 执行你的操作
        Log.i(TAG, "onReceive: current thread is ${Thread.currentThread().name}")
        Toast.makeText(context, "定时到了，哈哈", Toast.LENGTH_SHORT).show()

    }


}