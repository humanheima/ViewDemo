package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_text_switcher.*

/**
 * Created by dumingwei on 2020/4/2
 *
 * Desc: 测试TextSwitcher
 */

class TextSwitcherActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TextSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }

    val strs = arrayOf(
            "C++ 多态性 运算符重载",
            "C++ 多态性",
            "C++ 多态性 虚函数",
            "C++ 作用域分辨符"
    )

    var times = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_switcher)

        // 设置转换动画，这里引用系统自带动画
        // 设置转换动画，这里引用系统自带动画
        val `in`: Animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        val out: Animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
        tvSwitcher.inAnimation = `in`
        tvSwitcher.outAnimation = out

        tvSwitcher.setFactory {
            val textView = TextView(this)
            textView.textSize = 24f
            textView
        }

        tvSwitcher.setText(strs[times++ % strs.size])
    }

    fun next(view: View) {
        tvSwitcher.setText(strs[times++ % strs.size])
    }
}
