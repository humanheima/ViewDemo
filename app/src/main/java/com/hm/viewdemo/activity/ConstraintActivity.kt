package com.hm.viewdemo.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.view.View
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityConstraintAnimationBinding

/**
 * Created by p_dmweidu on 2025/1/10
 * Desc: 用来测试约束布局
 */
class ConstraintActivity : BaseActivity<ActivityConstraintAnimationBinding>() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ConstraintActivity::class.java)
            context.startActivity(starter)
        }
    }


//    fun initBinding(): Int {
//        return R.layout.activity_constraint_another
//    }

    /* override fun createViewBinding(): ActivityConstraintAnotherBinding {
         return ActivityConstraintAnotherBinding.inflate(layoutInflater)
     }
 */

    override fun createViewBinding(): ActivityConstraintAnimationBinding {
        return ActivityConstraintAnimationBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.btnStartVerticalAnimation.setOnClickListener {
            openAnimation()
        }
        //addView()
    }


    private fun openAnimation() {
        // 获取需要应用动画的 View
        val myView = binding.clNotOpenLayout

        // 创建 PropertyValuesHolder 实例来定义透明度的变化
        val alphaHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)

        // 创建 PropertyValuesHolder 实例来定义 Y 轴上的平移变化（假设向上移动 200px）
        val translateYHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -200f)

        // 使用 ObjectAnimator 和 PropertyValuesHolder 创建动画
        val animator = ObjectAnimator.ofPropertyValuesHolder(myView, alphaHolder, translateYHolder)

        // 设置动画时长（例如 500 毫秒）
        animator.duration = 1500

        // 添加监听器（可选）
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // 动画开始时的处理
            }

            override fun onAnimationEnd(animation: Animator) {
                // 动画结束时的处理
            }

            override fun onAnimationCancel(animation: Animator) {
                // 动画取消时的处理
            }

            override fun onAnimationRepeat(animation: Animator) {
                // 动画重复时的处理
            }
        })

        // 开始动画
        animator.start()
    }

}
