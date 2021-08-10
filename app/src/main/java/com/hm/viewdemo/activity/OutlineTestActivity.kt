package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.TestOutlineRoundDrawable


/**
 * Created by dumingwei on 2021/8/9
 *
 * Desc:
 */
class OutlineTestActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, OutlineTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var flTestOutline: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outline_test)
        flTestOutline = findViewById(R.id.flTestOutline)
        flTestOutline.background = TestOutlineRoundDrawable()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            flTestOutline.clipToOutline = true
        }

    }
}