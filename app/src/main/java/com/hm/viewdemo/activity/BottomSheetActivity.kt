package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.fragment.FullSheetDialogFragment
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_bottom_sheet.*

/**
 * Crete by dumingwei on 2019-06-24
 * Desc:
 */

class BottomSheetActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private lateinit var behavior: BottomSheetBehavior<*>

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, BottomSheetActivity::class.java)
            context.startActivity(starter)
        }
    }

    private val MIN_ALPHA = 0.75f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)
        setBackgroundAlpha(1.0f)

        for (i in 0..2) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_drag_slop, null)
            llcontent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        val layoutParams = scroll_bottom_sheet.layoutParams as CoordinatorLayout.LayoutParams
        if (llcontent.childCount > 4) {
            layoutParams.height = ScreenUtil.dpToPx(this, 400)
        } else {
            layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT

        }
        scroll_bottom_sheet.layoutParams = layoutParams
        behavior = BottomSheetBehavior.from(scroll_bottom_sheet)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.e(TAG, "onStateChanged$newState")
            }

            override fun onSlide(bottomSheet: View, onSlide: Float) {
                Log.e(TAG, "onSlide$onSlide")
                if (onSlide < 0.2f) {
                    setBackgroundAlpha(1.0f)
                } else {
                    var alpha = 1.0f - onSlide
                    if (alpha < MIN_ALPHA) {
                        alpha = MIN_ALPHA
                    }
                    setBackgroundAlpha(alpha)
                }
                //改变背景亮度
            }
        })
    }

    /**
     * @param 1.0 完全不透明；0.0 完全透明
     */
    private fun setBackgroundAlpha(alpha: Float) {
        val layoutParams: WindowManager.LayoutParams = window.attributes
        layoutParams.alpha = alpha
        window.attributes = layoutParams
    }

    fun intro(view: View) {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            setBackgroundAlpha(1.0f)
        } else if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            setBackgroundAlpha(MIN_ALPHA)
        }
    }

    fun select(view: View) {
        FullSheetDialogFragment().show(supportFragmentManager, "dialog")
    }

}
