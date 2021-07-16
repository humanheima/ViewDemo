package com.hm.viewdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.CircleMaskView

class DrawEveryThingActivity : AppCompatActivity() {


    private val TAG: String? = "DrawEveryThingActivity"

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, DrawEveryThingActivity::class.java)
            context.startActivity(intent)
        }
    }


    private lateinit var circleMaskView: CircleMaskView

    private lateinit var valueAnimator: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_every_thing)
        circleMaskView = findViewById(R.id.circle_mask_view)

        //初始半径，宽高的勾股定理，计算出直径是 382，半径就是191
        val startPx = ScreenUtil.dpToPx(this, 191)
        val finalPx = ScreenUtil.dpToPx(this, 14)

        //控件一半的距离
        val halfWidth = ScreenUtil.dpToPx(this, 249) / 2
        val halfHeight = ScreenUtil.dpToPx(this, 289) / 2

        circleMaskView.setSrcCanvasRadius(startPx)
        circleMaskView.setTranslateXDistance(startPx - halfWidth)
        //circleMaskView.setTranslateXDistance(halfWidth)
        circleMaskView.setTranslateYDistance(startPx - halfHeight)
        //circleMaskView.setTranslateYDistance(halfHeight)
        circleMaskView.setInnerRadius(startPx)
        circleMaskView.post {

            valueAnimator = ValueAnimator.ofInt(startPx, finalPx)
            valueAnimator.duration = 3000
            valueAnimator.addUpdateListener {
                val animatedValue = it.animatedValue as Int
                Log.i(TAG, "onCreate: it.animatedValue =  $animatedValue")
                //circleMaskView.setPaintWidth(animatedValue)
                circleMaskView.setInnerRadiusAndInValidate(animatedValue)
            }
            //valueAnimator.startDelay = 2000
            valueAnimator.start()
        }
    }


}