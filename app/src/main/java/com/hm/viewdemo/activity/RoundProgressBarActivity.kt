package com.hm.viewdemo.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.RoundProgressView

/**
 * Created by dumingwei on 2021/7/19
 *
 * Desc: 圆角横向进度条，进度也是圆角的
 */
class RoundProgressBarActivity : AppCompatActivity() {


    private lateinit var progressView: RoundProgressView

    private var animator: ValueAnimator? = null

    private val TAG: String = "RoundProgressBarActivit"

    private var controller: SeekBar? = null

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, RoundProgressBarActivity::class.java)
            context.startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_progress_bar)
        progressView = findViewById(R.id.roundProgressView)

        //progressView.translationX = -100f

        progressView.post {
            val width = progressView.measuredWidth


            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.interpolator = LinearInterpolator()
            animator.addUpdateListener {
                val value = it.animatedValue as Float
                val scrollX = -(value * width).toInt()
                Log.i(TAG, "onCreate: scrollX = $scrollX")
                //progressView.scrollX = scrollX
                progressView.scrollTo(scrollX, 0)

            }
            animator.duration = 5000
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.RESTART
            animator.start()


            val translateAnimator = ObjectAnimator.ofFloat(progressView, "translationX", -width.toFloat(), 0f)
            translateAnimator.duration = 5000
            translateAnimator.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()
    }
}