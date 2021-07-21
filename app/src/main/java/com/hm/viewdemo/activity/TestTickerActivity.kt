package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_test_ticker.*

class TestTickerActivity : AppCompatActivity() {

    private val TAG: String? = "TestTickerActivity"

    lateinit var handler: Handler
    val strs = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    var index3 = 0
    var index2 = 0
    var index1 = 0
    var index0 = 0


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TestTickerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_ticker)


        for (i in strs) {
            tickerView0.setCharacterLists(i)
            tickerView1.setCharacterLists(i)
            tickerView2.setCharacterLists(i)
            tickerView3.setCharacterLists(i)
        }

        tickerView0.text = strs[0]
        tickerView1.text = strs[0]
        tickerView2.text = strs[0]
        tickerView3.text = strs[0]


        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.i(TAG, "handleMessage: ")
                //handler.sendEmptyMessageDelayed(1, 200)
                when (msg.what) {
                    0 -> next0()
                    1 -> next1()
                    2 -> next2()
                    3 -> next3()
                }

            }
        }

        btnStart.setOnClickListener {
            index0 = 0
            index1 = 0
            index2 = 0
            index3 = 0
            next0()
            next1()
            next2()
            next3()
        }
    }

    fun next0() {
        if (index0 < strs.size) {
            tickerView0.text = strs[index0++]
            handler.sendEmptyMessageDelayed(0, 50)
        }
    }

    fun next1() {
        if (index1 < strs.size) {
            tickerView1.text = strs[index1++]
            handler.sendEmptyMessageDelayed(1, 50)
        }
    }

    fun next2() {
        if (index2 < strs.size) {
            tickerView2.text = strs[index2++]
            handler.sendEmptyMessageDelayed(2, 50)
        }
    }

    fun next3() {
        if (index3 < strs.size) {
            tickerView3.text = strs[index3++]
            handler.sendEmptyMessageDelayed(3, 50)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
