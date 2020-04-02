package com.hm.viewdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class TestScreenScrollViewActivity : AppCompatActivity() {

        companion object {

            fun launch(context: Context) {
                val intent = Intent(context, TestScreenScrollViewActivity::class.java)
                context.startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_screen_scroll_view)
    }
}
