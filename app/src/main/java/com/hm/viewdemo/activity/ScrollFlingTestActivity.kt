package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

class ScrollFlingTestActivity : AppCompatActivity() {

    companion object {

        private val TAG: String = "ScrollFlingTestActivity"

        fun launch(context: Context) {
            val intent = Intent(context, ScrollFlingTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_fling_test)

//        for (index in 0..100) {
//            val button = Button(this)
//            button.text = "button$index"
//            val dpToPx = ScreenUtil.dpToPx(this, 50)
//            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                dpToPx
//            )
//            ll_content.addView(button, layoutParams)
//        }
//        ll_content.post {
//            Log.i(TAG, "onCreate: ${simple_scroll_view.measuredHeight}")
//            Log.i(TAG, "onCreate: ${ll_content.measuredHeight}")
//
//        }

    }

}