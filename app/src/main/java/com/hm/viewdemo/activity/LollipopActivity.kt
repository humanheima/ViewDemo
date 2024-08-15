package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityLollipopBinding

/**
 * Crete by dumingwei on 2019-06-06
 * Desc:
 *
 */
class LollipopActivity : BaseActivity<ActivityLollipopBinding>() {


    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, LollipopActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityLollipopBinding {
        return ActivityLollipopBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.btnStartAnim.setOnClickListener {
            binding.lollipop.startAnim()
        }

        binding.btnCancelAnim.setOnClickListener {
            binding.lollipop.stopAnim()
        }

        binding.btnChangeConstraint.setOnClickListener {
            changeConstraint()
        }
    }


    private fun changeConstraint() {
        val set = ConstraintSet()
        set.clone(binding.clRoot)
        set.setGuidelinePercent(R.id.guideLine, 0.4f)
        set.applyTo(binding.clRoot)
        TransitionManager.beginDelayedTransition(binding.clRoot)
    }
}
