package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.transition.TransitionManager
import android.widget.ImageView
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityConstraintAnotherBinding
import kotlinx.android.synthetic.main.activity_constraint_another.*

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
        buttonChangeConstraint.setOnClickListener {
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
         cl_root.addView(line, layoutParams)

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
         cl_root.addView(imageView, imageLayoutParams)

    }

    private fun changeConstraint() {
        val set = ConstraintSet()
        set.clone(cl_root)
        set.setGuidelinePercent(R.id.guideLine, 0.3F)
        TransitionManager.beginDelayedTransition(cl_root)
        set.applyTo(cl_root)
    }

}
