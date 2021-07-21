package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_scroll_fling_test.*

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

        for (index in 0..50) {
            val button = Button(this)
            button.text = "button$index"
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            ll_content.addView(button, layoutParams)
        }
        ll_content.post {
            Log.i(TAG, "onCreate: ${simple_scroll_view.measuredHeight}")
            Log.i(TAG, "onCreate: ${ll_content.measuredHeight}")

        }

    }

}