package com.hm.viewdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivitySeekBarTestBinding

/**
 * Created by dumingwei on 2020/12/18
 * Desc: 测试SeekBar
 * 1. 设置 progressDrawable 和设置 background 的区别。 可以在 progressDrawable 中，同时设置背景，第一进度背景，第二进度背景。
 * 2. splitTrack 属性应该与 progressDrawable 配合才起作用。
 * Android 的 SeekBar 中，splitTrack 属性用于控制进度条是否在滑块（thumb）处显示一个“间隙”或“分割”。
 * 具体来说，当 splitTrack 设置为 true 时，进度条的背景（track）会在滑块的位置断开，露出底层的背景颜色，从而在视觉上将进度条分为两部分。
 * 这个效果在某些自定义样式中可以增强用户体验，尤其是在需要突出滑块位置时。
 */
class SeekBarTestActivity : AppCompatActivity() {

    private val TAG: String = "SeekBarTestActivity"

    private lateinit var binding: ActivitySeekBarTestBinding

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

        binding.seekBarOne.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.d(TAG, "onProgressChanged: p = $p1")
                binding.seekBarOne.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.d(TAG, "onStartTrackingTouch: ")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.d(TAG, "onStopTrackingTouch: ")
            }
        })
    }

}