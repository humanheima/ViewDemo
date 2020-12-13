package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityTestWaveViewBinding
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.WaveView

/**
 * Created by dumingwei on 2020/12/11
 *
 * Desc: 测试呼吸动效的View
 */
class TestWaveViewActivity : AppCompatActivity() {

    private val TAG: String = "TestWaveViewActivity"

    private lateinit var binding: ActivityTestWaveViewBinding

    private lateinit var textView: TextView
    private lateinit var waveView: WaveView


    private lateinit var circleAnimation1: Animation
    private lateinit var circleAnimation2: Animation
    private lateinit var circleAnimation3: Animation

    private lateinit var roundRectAnimation1: Animation
    private lateinit var roundRectAnimation2: Animation
    private lateinit var roundRectAnimation3: Animation

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TestWaveViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestWaveViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textView = binding.mainAlbumSingleSubscribeTv
        waveView = binding.waveView

        val layoutParams = waveView.layoutParams
        textView.post {
            val width = textView.measuredWidth
            if (layoutParams is ViewGroup.MarginLayoutParams) {
                layoutParams.width = width + ScreenUtil.dpToPx(this, 26)
                waveView.layoutParams = layoutParams
                waveView.start()
            }
        }

        circleAnimation1 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        circleAnimation2 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        circleAnimation3 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)


        roundRectAnimation1 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        roundRectAnimation2 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)
        roundRectAnimation3 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha)

        circleAnimation3.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                startCircleScanAnim()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })


        roundRectAnimation3.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                startRoundScanAnim()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        binding.flRoundRectAnimInclude.tvRoundRectStartScan.setOnClickListener {
            startRoundScanAnim()
        }

    }

    private fun startCircleScanAnim() {
        binding.flScanAnimInclude.circle1.clearAnimation()
        binding.flScanAnimInclude.circle1.clearAnimation()
        binding.flScanAnimInclude.circle1.clearAnimation()

        binding.flScanAnimInclude.circle1.startAnimation(circleAnimation1)

        circleAnimation2.startOffset = 1000
        binding.flScanAnimInclude.circle2.startAnimation(circleAnimation2)

        circleAnimation3.startOffset = 2000
        binding.flScanAnimInclude.circle3.startAnimation(circleAnimation3)
    }

    private fun startRoundScanAnim() {
        binding.flRoundRectAnimInclude.roundRect1.clearAnimation()
        binding.flRoundRectAnimInclude.roundRect2.clearAnimation()
        binding.flRoundRectAnimInclude.roundRect3.clearAnimation()

        binding.flRoundRectAnimInclude.roundRect1.startAnimation(roundRectAnimation1)

        roundRectAnimation2.startOffset = 1000
        binding.flRoundRectAnimInclude.roundRect2.startAnimation(roundRectAnimation2)

        roundRectAnimation3.startOffset = 2000
        binding.flRoundRectAnimInclude.roundRect3.startAnimation(roundRectAnimation3)
    }

    override fun onResume() {
        super.onResume()
        waveView.start()
    }

    override fun onPause() {
        super.onPause()
        waveView.stop()
    }

}