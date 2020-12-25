package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_announcement.*

/**
 * Crete by dumingwei on 2019-05-27
 * Desc: 自动滚动的公告条
 *
 */
class AnnouncementActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, AnnouncementActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)
        //flipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in)
        //flipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_out)
        flipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
        flipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_top)
        flipper.startFlipping()

        for (i in 0..10) {
            val textView = getTextView("hello world$i")
            flipperDynamic.addView(textView)
        }
        flipperDynamic.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
        flipperDynamic.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_top)
        //flipperDynamic.startFlipping()

        btnDynamicAddView.setOnClickListener {
            //flipper.startFlipping()
            flipperDynamic.startFlipping()
        }
    }

    private fun getTextView(text: String): TextView {
        val textView = TextView(this)

        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtil.dpToPx(this, 48))

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        textView.text = text
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.gravity = Gravity.CENTER
        textView.maxLines = 1
        return textView

    }
}
