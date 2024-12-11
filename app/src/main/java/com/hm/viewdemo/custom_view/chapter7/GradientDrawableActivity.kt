package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityGradientDrawableBinding
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.AnimatedGradientDrawable

/**
 * Created by dumingwei on 2020/5/22
 *
 * Desc: GradientDrawable的使用
 *
 */
class GradientDrawableActivity : BaseActivity<ActivityGradientDrawableBinding>() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, GradientDrawableActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityGradientDrawableBinding {
        return ActivityGradientDrawableBinding.inflate(layoutInflater)
    }

    override fun initData() {
//        val background = GradientDrawable(
//            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
//                resources.getColor(R.color.vip_start_color),
//                resources.getColor(
//                    R.color.vip_end_color
//                )
//            )
//        )

        val animatedGradientDrawable = AnimatedGradientDrawable()
        animatedGradientDrawable.cornerRadius = ScreenUtil.dpToPx(this, 10)
        binding.ivAnimatorGradientDrawable.setImageDrawable(animatedGradientDrawable)

        val background = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                resources.getColor(R.color.xx_color_99a1fd),
                resources.getColor(
                    R.color.xx_color_e0bad0
                ),

                resources.getColor(
                    R.color.xx_color_00e4cdac
                )
            )
        )

        binding.ivFirst.background = background

        val background1 = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(
                resources.getColor(R.color.xx_color_6699a1fd),
                resources.getColor(
                    R.color.xx_color_66e0bad0
                ),

                resources.getColor(
                    R.color.xx_color_6600e4cdac
                )
            )
        )
        binding.ivSecond.background = background1
    }
}
