package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_lollipop.*

/**
 * Crete by dumingwei on 2019-06-06
 * Desc:
 *
 */
class LollipopActivity : AppCompatActivity() {


    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, LollipopActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lollipop)
        btnStartAnim.setOnClickListener {
            lollipop.startAnim()
        }

        btnCancelAnim.setOnClickListener {
            lollipop.stopAnim()
        }

        btnChangeConstraint.setOnClickListener {
            changeConstraint()
        }

    }

    private fun changeConstraint() {
        val set = ConstraintSet()
        set.clone(clRoot)
        set.setGuidelinePercent(R.id.guideLine, 0.4f)
        set.applyTo(clRoot)
        TransitionManager.beginDelayedTransition(clRoot)
    }
}
