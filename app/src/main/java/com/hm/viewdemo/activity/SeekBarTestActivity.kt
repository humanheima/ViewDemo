package com.hm.viewdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.LinearInterpolator
import com.hm.viewdemo.databinding.ActivitySeekBarTestBinding
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by dumingwei on 2020/12/18
 *
 * Desc: 测试SeekBar
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
            val width = paint.measureText(it)
            Log.d(TAG, "testTextWidth: $it is $width")
        }

    }
}