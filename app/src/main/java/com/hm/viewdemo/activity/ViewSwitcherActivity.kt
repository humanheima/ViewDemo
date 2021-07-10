package com.hm.viewdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import com.hm.viewdemo.R


/**
 * Created by dumingwei on 2021/7/6
 *
 * Desc:测试ViewSwitcher使用 图片加文字的布局
 */
class ViewSwitcherActivity : AppCompatActivity() {


    private val TAG: String = "ViewSwitcherActivity"

    private lateinit var viewSwitcher: ViewSwitcher

    var strs4 = listOf("昔我往矣", "杨柳依依", "今我来思", "雨雪霏霏", "黄河远上白云间", "一片孤城万仞山", "羌笛何须怨杨柳", "春风不度玉门关")

    private lateinit var handler: Handler
    private var indexSingleTextStart = 0

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ViewSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_switcher)
        viewSwitcher = findViewById(R.id.view_switcher)

        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        val inAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.text_switcher_slide_in_from_bottom)
        val outAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.text_switcher_slide_out_to_top)
        inAnim.duration = 500
        outAnim.duration = 500

        viewSwitcher.outAnimation = outAnim
        viewSwitcher.inAnimation = inAnim

        viewSwitcher.setFactory(object : ViewSwitcher.ViewFactory {

            override fun makeView(): View {
                return View.inflate(this@ViewSwitcherActivity, R.layout.view_switcher_imageview_text, null)
            }
        })

        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    4 -> {
                        nextSingleText()
                    }
                }
            }
        }

        btnStart.setOnClickListener {
            handler.sendEmptyMessage(4)
        }

        btnStop.setOnClickListener {
            handler.removeMessages(4)
        }


        val iv = viewSwitcher.currentView.findViewById<ImageView>(R.id.view_swticher_iv)
        val tv = viewSwitcher.currentView.findViewById<TextView>(R.id.view_swticher_tv)
        iv.setImageResource(R.drawable.ic_close)
        tv.text = strs4[indexSingleTextStart++]
        viewSwitcher.showNext()
    }

    private fun nextSingleText() {
        if (indexSingleTextStart < strs4.size) {
            Log.i(TAG, "nextSingleText: ")

            val iv = viewSwitcher.findViewById<ImageView>(R.id.view_swticher_iv)
            val tv = viewSwitcher.findViewById<TextView>(R.id.view_swticher_tv)
            iv.setImageResource(R.drawable.ic_close)
            tv.text = strs4[indexSingleTextStart++]
            viewSwitcher.showNext()
        } else {
            indexSingleTextStart = 0
        }
        handler.sendEmptyMessageDelayed(4, 3000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeMessages(4)
    }
}