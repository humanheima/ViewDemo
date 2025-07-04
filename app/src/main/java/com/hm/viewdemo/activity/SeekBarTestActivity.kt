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

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

}