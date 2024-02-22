package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.view_lollipop.view.*

/**
 * Created by ff.zhang on 16/10/11.
 */
class LollipopView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var ad: AnimationDrawable? = null

    private val mHandler = Handler()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_lollipop, this)


    }

    fun addTopView(view: View) {
        flTop.addView(view)
    }

    fun startAnim() {
        ivCenterCircle.setImageResource(R.drawable.ic_refresh_loading)
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_image_rotate)
        if (animation != null) {
            val lin = LinearInterpolator()
            animation.interpolator = lin
        }

        try {
            ivCenterCircle.startAnimation(animation)
        } catch (e: Exception) {
            Log.e("TargetCenterView", e.message ?: "")
        }

    }

    fun stopAnim() {
        ivCenterCircle.clearAnimation()
        initCloseAnim()
    }

    private fun initCloseAnim() {
        llCircle.visibility = View.GONE
        ivLine.setImageResource(R.drawable.anim_target)
        if (ad != null && ad?.isRunning == true) {
            ad?.stop()
        }
        ad = ivLine.drawable as AnimationDrawable
        if (ad != null) {
            ad!!.start()
        }
        mHandler.postDelayed({
            ivCenterCircle.setImageResource(R.drawable.ic_refresh_circle_white)
            ivLine!!.setImageResource(R.drawable.ic_refresh_line)
            llCircle.visibility = View.VISIBLE
        }, 300)
    }

    fun destroy() {
        ivCenterCircle.clearAnimation()
    }

}
