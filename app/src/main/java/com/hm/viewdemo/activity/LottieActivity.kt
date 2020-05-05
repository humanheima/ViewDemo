package com.hm.viewdemo.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.airbnb.lottie.*
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_lottie.*

/**
 * Created by dumingwei on 2020/4/16
 *
 * Desc: 测试 lottie
 */
class LottieActivity : AppCompatActivity() {

    private val TAG: String? = "LottieActivity"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LottieActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)

        lottieViewLoading.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                //Log.d(TAG, "onAnimationRepeat: ")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.d(TAG, "onAnimationEnd: ")
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.d(TAG, "onAnimationCancel: ")
            }

            override fun onAnimationStart(animation: Animator?) {
                Log.d(TAG, "onAnimationStart: ")
            }

        })

        btnStartLightAnim.setOnClickListener {
            lottieViewLight.playAnimation()
        }

        loadFromUrl()

        dynamicLoad1()

        useLottieDrawable()

        testLottieSize()

    }

    private fun testLottieSize() {
        lottieMarquee.setAnimation("lottie/marquee.json")
        lottieMarquee.playAnimation()

        lottieMarquee1.setAnimation("lottie/marquee1.json")
        lottieMarquee1.playAnimation()


    }

    private fun loadFromUrl() {
        lottieLoadFromUrl.setAnimationFromUrl("https://cqz-1256838880.cos.ap-shanghai.myqcloud.com/bird1.json")
        lottieLoadFromUrl.repeatMode = LottieDrawable.REVERSE
        lottieLoadFromUrl.repeatCount = LottieDrawable.INFINITE
        lottieLoadFromUrl.playAnimation()
    }

    private fun dynamicLoad1() {
        lottieLoadDynamic1.setAnimation("lottie/rotate/anim_3.json")
        lottieLoadDynamic1.repeatMode = LottieDrawable.RESTART
        lottieLoadDynamic1.repeatCount = LottieDrawable.INFINITE
        lottieLoadDynamic1.playAnimation()
    }

    private fun useLottieDrawable() {

        val drawable = LottieDrawable()
        LottieCompositionFactory.fromAsset(this,
                "lottie/loading_1/loading.json").addListener(object : LottieListener<LottieComposition> {
            override fun onResult(result: LottieComposition?) {
                Log.d(TAG, "onResult: $result")
                result?.let {

                    drawable.composition = it
                    drawable.repeatMode = LottieDrawable.RESTART
                    drawable.repeatCount = LottieDrawable.INFINITE
                    drawable.playAnimation()
                    lottieUseDrawable.setImageDrawable(drawable)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        lottieMarquee.pauseAnimation()
        lottieMarquee1.pauseAnimation()
    }

}
