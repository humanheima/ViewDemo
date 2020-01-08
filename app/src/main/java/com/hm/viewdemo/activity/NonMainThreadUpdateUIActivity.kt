package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_non_main_thread_update_ui.*
import kotlin.concurrent.thread

/**
 * Crete by dumingwei on 2019-12-14
 * Desc: 测试在非UI线程更新UI
 *
 */
class NonMainThreadUpdateUIActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NonMainThreadUpdateUIActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_non_main_thread_update_ui)

        thread(true) {
            tvText.text = "在子线程更新UI"
        }

    }
}
