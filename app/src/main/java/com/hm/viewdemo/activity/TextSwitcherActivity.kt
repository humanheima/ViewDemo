package com.hm.viewdemo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_text_switcher.*
import kotlin.collections.ArrayList

/**
 * Created by dumingwei on 2020/4/2
 *
 * Desc: 测试TextSwitcher
 */

class TextSwitcherActivity : AppCompatActivity() {

    private val TAG: String? = "TextSwitcherActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TextSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }

    val strs = arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    var strs0 = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    var strs1 = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    var strs2 = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    var strs3 = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    private val textSwitcherList: ArrayList<TextSwitcher> = ArrayList()

    var index0Start = 0
    var index0End = 0

    var index1Start = 0
    var index1End = 0

    var index2Start = 0
    var index2End = 0


    var index3Start = 0
    var index3End = 0

    var amount = 5000

    /**
     * 总共的位数，个位十位百位千位
     */
    var size = 0

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_switcher)

        textSwitcherList.add(textSwitcher0)
        textSwitcherList.add(textSwitcher1)
        textSwitcherList.add(textSwitcher2)
        textSwitcherList.add(textSwitcher3)

        initTextSwitcher()

        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    0 -> {
                        next0()
                    }
                    1 -> {
                        //更新十位数的动画
                        next1()
                    }
                    2 -> {
                        //更新百位数的动画
                        next2()
                    }
                    3 -> {
                        //更新千位数的动画
                        next3()
                    }
                }
                Log.d(TAG, "handleMessage: ")

            }
        }

        btnStart.setOnClickListener {
            startAnimation(3)
            //amount *= 10
        }
    }

    private fun initTextSwitcher() {
        val inAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.text_switcher_slide_in_from_bottom)
        val outAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.text_switcher_slide_out_to_top)

        inAnim.duration = 50
        outAnim.duration = 50
        textSwitcher1.inAnimation = inAnim
        textSwitcher1.outAnimation = outAnim

        val factory = object : ViewSwitcher.ViewFactory {
            override fun makeView(): View {
                val textView = TextView(this@TextSwitcherActivity)
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER_VERTICAL
                textView.layoutParams = layoutParams
                textView.textSize = 24f
                textView.gravity = Gravity.CENTER
                return textView
            }
        }
        textSwitcher0.setFactory(factory)
        textSwitcher1.setFactory(factory)
        textSwitcher2.setFactory(factory)
        textSwitcher3.setFactory(factory)

        textSwitcher3.setCurrentText("0")
        textSwitcher2.setCurrentText("0")
        textSwitcher1.setCurrentText("0")
        textSwitcher0.setCurrentText("0")

        val amountStr = amount.toString().reversed()
        val size = amountStr.length

        for (i in 0 until size) {
            Log.d(TAG, "initTextSwitcher: ${amountStr[i]}")
            if (i < textSwitcherList.size) {
                textSwitcherList[i].setCurrentText(amountStr[i].toString())
            }
        }
    }

    private fun startAnimation(rate: Int) {
        textSwitcher3.setCurrentText("0")
        textSwitcher2.setCurrentText("0")
        textSwitcher1.setCurrentText("0")
        textSwitcher0.setCurrentText("0")

        index0Start = 0
        index0End = 0

        index1Start = 0
        index1End = 0

        index2Start = 0
        index2End = 0


        index3Start = 0
        index3End = 0

        //翻倍后的金币数，最大要小于10000
        val rateAmount = (amount * rate)

        //个位数
        val singleDigit = rateAmount % 10
        //十位数
        val tenDigit = (rateAmount % 100) / 10
        //百位数
        val hundredDigit = (rateAmount % 1000) / 100
        //千位数
        val thousandDigit = (rateAmount % 10000) / 1000

        val rateAmountStr = (amount * rate).toString()
        //最多到几位数
        val size = rateAmountStr.length

        if (size == 4) {
            if (singleDigit > 0) {
                strs0 = strs.subList(0, singleDigit + 1)
            } else {
                strs0 = strs
            }
            next0()
            if (tenDigit > 0) {
                strs1 = strs.subList(0, tenDigit + 1)
            } else {
                strs1 = strs
            }
            handler.postDelayed({
                next1()
            }, 200)

            if (hundredDigit > 0) {
                strs2 = strs.subList(0, hundredDigit + 1)
            } else {
                strs2 = strs
            }
            handler.postDelayed({
                next2()
            }, 400)

            if (thousandDigit > 0) {
                strs3 = strs.subList(0, thousandDigit + 1)
            }
            handler.postDelayed({
                next3()
            }, 600)

        } else if (size == 3) {
            if (singleDigit > 0) {
                strs0 = strs.subList(0, singleDigit + 1)
            } else {
                strs0 = strs
            }
            next0()
            if (tenDigit > 0) {
                strs1 = strs.subList(0, tenDigit + 1)
            } else {
                strs1 = strs
            }
            handler.postDelayed({
                next1()
            }, 200)

            strs2 = strs.subList(0, hundredDigit + 1)
            handler.postDelayed({
                next2()
            }, 400)

        } else if (size == 2) {
            if (singleDigit > 0) {
                strs0 = strs.subList(0, singleDigit + 1)
            } else {
                strs0 = strs
            }
            next0()
            strs1 = strs.subList(0, tenDigit + 1)
            handler.postDelayed({
                next1()
            }, 200)

        } else if (size == 1) {//只有个位数，只转动个位就行
            strs0 = strs.subList(0, singleDigit + 1)
            next0()
        }
    }

    private fun next0() {
        if (index0Start < strs0.size) {
            textSwitcher0.setText(strs0[index0Start++])
            handler.sendEmptyMessageDelayed(0, 50)
        }
    }

    private fun next1() {
        if (index1Start < strs1.size) {
            textSwitcher1.setText(strs1[index1Start++])
            handler.sendEmptyMessageDelayed(1, 50)
        }
    }

    private fun next2() {
        if (index2Start < strs2.size) {
            textSwitcher2.setText(strs2[index2Start++])
            handler.sendEmptyMessageDelayed(2, 50)
        }
    }

    private fun next3() {
        if (index3Start < strs3.size) {
            textSwitcher3.setText(strs3[index3Start++])
            handler.sendEmptyMessageDelayed(3, 50)
        }
    }

}
