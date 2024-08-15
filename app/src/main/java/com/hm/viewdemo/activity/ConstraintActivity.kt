package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.transition.TransitionManager
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityConstraintAnotherBinding

class ConstraintActivity : BaseActivity<ActivityConstraintAnotherBinding>() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ConstraintActivity::class.java)
            context.startActivity(starter)
        }
    }


//    fun initBinding(): Int {
//        return R.layout.activity_constraint_another
//    }

    override fun createViewBinding(): ActivityConstraintAnotherBinding {
        return ActivityConstraintAnotherBinding.inflate(layoutInflater)
    }


    override fun initData() {
        binding.buttonChangeConstraint.setOnClickListener {
            changeConstraint()
        }
        //addView()
    }

    private fun addView() {
        val line: Guideline = Guideline(this)
        line.id = R.id.guide_line_id
        val layoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.guidePercent = 0.3F
        layoutParams.orientation = ConstraintLayout.LayoutParams.HORIZONTAL
        binding.clRoot.addView(line, layoutParams)

        val imageView: ImageView = ImageView(this)
        imageView.setImageResource(R.drawable.ic_gear_small)
        val imageLayoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        imageLayoutParams.startToStart = R.id.cl_root
        imageLayoutParams.endToEnd = R.id.cl_root
        imageLayoutParams.bottomToTop = R.id.guide_line_id
        layoutParams.orientation = ConstraintLayout.LayoutParams.HORIZONTAL
        binding.clRoot.addView(imageView, imageLayoutParams)

    }

    private fun changeConstraint() {
        val set = ConstraintSet()
        set.clone(binding.clRoot)
        set.setGuidelinePercent(R.id.guideLine, 0.3F)
        TransitionManager.beginDelayedTransition(binding.clRoot)
        set.applyTo(binding.clRoot)
    }

}
