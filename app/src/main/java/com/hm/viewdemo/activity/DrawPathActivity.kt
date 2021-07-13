package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.draw.DrawPathView

/**
 * Crete by dumingwei on 2019-08-12
 * Desc: 学习Path的基本操作
 * 参考: https://www.gcssloop.com/customview/Path_Basic/
 *
 */
class DrawPathActivity : AppCompatActivity() {


    private val TAG: String = "DrawPathActivity"

    private lateinit var drawEverythingView: DrawPathView

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, DrawPathActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_path)
        drawEverythingView = findViewById(R.id.drawEverythingView)


        drawEverythingView.post {
            val toFloat = drawEverythingView.measuredWidth.toFloat()
            Log.i(TAG, "onCreate: toFloat = ${toFloat}")

            val translateAnimation = TranslateAnimation(0f, -toFloat, 0f, 0f)


            val animation = TranslateAnimation(Animation.ABSOLUTE, 0f,
                    Animation.ABSOLUTE, 800f, Animation.ABSOLUTE, 0f
                    , Animation.ABSOLUTE, 0f)
            animation.duration = 1000
            animation.fillAfter = true
            drawEverythingView.startAnimation(animation)

//
//            val curTranslationX: Float = drawEverythingView.translationX
//            val animator: ObjectAnimator = ObjectAnimator.ofFloat(drawEverythingView, "translationX",
//                    curTranslationX, toFloat, curTranslationX)
//            animator.duration = 5000
//            animator.startDelay = 3000
//            animator.start()


        }
    }
}
