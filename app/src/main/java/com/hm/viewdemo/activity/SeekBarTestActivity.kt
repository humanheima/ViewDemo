package com.hm.viewdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.animation.LinearInterpolator
import com.hm.viewdemo.databinding.ActivitySeekBarTestBinding
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by dumingwei on 2020/12/18
 *
 * Desc: 测试SeekBar
 * 先把查看原文加进去
 *
 * 1. 获取TextView 一行的宽度  oneLineWidth 和 两行的宽度 twoLineWidth
 * 2. 获取 "原文>" 的宽度 suffixWidth
 * 3. 获取 一行剩余的宽度  oneLineLeftWidth =  oneLineWidth - suffixWidth
 * 4. 获取 2行剩余的宽度 twoLineWidth - suffixWidth
 *
 * 6. 如果原文宽度 少于 twoLineLeftWidth ，直接加进去
 *
 * 7. 如果6不满足，一个字一个字的加进去
 *
 * 原文>
 */


/**
 * 另外一种方法
 * 获取TextView 一行的宽度  oneLineWidth 和 两行的宽度 twoLineWidth
 * 直接把 原文> 作为一个viewGroup，获取它的宽度：suffixWidth。
 * 那么原文内容，最多的宽度就是 twoLineWidth - suffixWidth，超过这个宽度，就用截断几个字符，并加上...
 *
 * 保证截断字符加上... 之后的总长度小于等于 twoLineWidth - suffixWidth
 *
 */
class SeekBarTestActivity : AppCompatActivity() {

    private val TAG: String = "SeekBarTestActivity"

    private val list = arrayListOf<String>()

    private lateinit var binding: ActivitySeekBarTestBinding

    private val paint = Paint()

    private lateinit var animator: ValueAnimator

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, SeekBarTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekBarTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (i in 0..9) {
            list.add("$i")
        }
        list.add("你")
        list.add("我")
        list.add("他")
        list.add("...")

        paint.textSize = ScreenUtil.spToPx(this, 14).toFloat() //设置字体大小

        binding.btnMeasureTextWidth.setOnClickListener {
            testTextWidth()
        }

        animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 15000
        animator.interpolator = LinearInterpolator()

        binding.seekBar.progress = 50
        binding.seekBar.secondaryProgress = 80

        animator.addUpdateListener {
            val percent: Int = it.animatedValue as Int
            binding.seekBar.progress = percent
        }

        binding.btnSetProgress.setOnClickListener {
            animator.start()
        }
    }

    private fun testTextWidth() {
        list.forEach {
            val length = it.length
            val width = paint.measureText(it, 0, length)
            Log.d(TAG, "testTextWidth: $length = $length , width =  $width")
        }

    }
}