package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityAnnouncementBinding
import com.hm.viewdemo.util.ScreenUtil

/**
 * Crete by dumingwei on 2019-05-27
 * Desc: 自动滚动的公告条，TextSwitcherActivity也可以看一下
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

    private lateinit var binding: ActivityAnnouncementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //flipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in)
        //flipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_out)
        binding.flipper.inAnimation =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
        binding.flipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_top)
        binding.flipper.startFlipping()

        for (i in 0..10) {
            val textView = getTextView("hello world$i")
            binding.flipperDynamic.addView(textView)
        }
        binding.flipperDynamic.inAnimation =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
        binding.flipperDynamic.outAnimation =
            AnimationUtils.loadAnimation(this, R.anim.slide_out_to_top)
        //flipperDynamic.startFlipping()

        binding.btnDynamicAddView.setOnClickListener {
            //flipper.startFlipping()
            binding.flipperDynamic.startFlipping()
        }
    }

    private fun getTextView(text: String): TextView {
        val textView = TextView(this)

        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ScreenUtil.dpToPx(this, 48)
        )

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        textView.text = text
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.gravity = Gravity.CENTER
        textView.maxLines = 1
        return textView

    }
}
