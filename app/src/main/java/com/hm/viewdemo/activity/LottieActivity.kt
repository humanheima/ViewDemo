package com.hm.viewdemo.activity

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.util.Log
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieListener
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityLottieBinding

/**
 * Created by dumingwei on 2020/4/16
 *
 * Desc: 测试 lottie
 */
class LottieActivity : BaseActivity<ActivityLottieBinding>() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LottieActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityLottieBinding {
        return ActivityLottieBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.lottieViewLoading.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        binding.btnStartLightAnim.setOnClickListener {
            binding.lottieViewLight.playAnimation()
        }

        loadFromUrl()

        dynamicLoad1()

        useLottieDrawable()

        testLottieSize()
    }


    private fun testLottieSize() {
        binding.lottieMarquee.setAnimation("lottie/marquee.json")
        binding.lottieMarquee.playAnimation()

        binding.lottieMarquee1.setAnimation("lottie/marquee1.json")
        binding.lottieMarquee1.playAnimation()


    }

    private fun loadFromUrl() {
        binding.lottieLoadFromUrl.setAnimationFromUrl("https://cqz-1256838880.cos.ap-shanghai.myqcloud.com/bird1.json")
        binding.lottieLoadFromUrl.repeatMode = LottieDrawable.REVERSE
        binding.lottieLoadFromUrl.repeatCount = LottieDrawable.INFINITE
        binding.lottieLoadFromUrl.playAnimation()
    }

    private fun dynamicLoad1() {
        //lottieLoadDynamic1.setAnimation("lottie/rotate/anim_rorate_play_stretch_test_1.json")
        binding.lottieLoadDynamic1.setAnimation("bubble4/xa.json")
        binding.lottieLoadDynamic1.repeatMode = LottieDrawable.RESTART
        binding.lottieLoadDynamic1.repeatCount = LottieDrawable.INFINITE
        binding.lottieLoadDynamic1.playAnimation()
    }

    private fun useLottieDrawable() {

        val drawable = LottieDrawable()
        LottieCompositionFactory.fromAsset(
            this,
            "lottie/loading_1/loading.json"
        ).addListener(object : LottieListener<LottieComposition> {
            override fun onResult(result: LottieComposition?) {
                Log.i(TAG, "onResult: $result")
                result?.let {

                    drawable.composition = it
                    drawable.repeatMode = LottieDrawable.RESTART
                    drawable.repeatCount = LottieDrawable.INFINITE
                    drawable.playAnimation()
                    binding.lottieUseDrawable.setImageDrawable(drawable)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        binding.lottieMarquee.pauseAnimation()
        binding.lottieMarquee1.pauseAnimation()
    }

}
