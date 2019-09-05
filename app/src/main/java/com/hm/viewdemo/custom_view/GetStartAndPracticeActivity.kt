package com.hm.viewdemo.custom_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hm.viewdemo.R
import com.hm.viewdemo.custom_view.chapter7.Chapter7Activity
import com.hm.viewdemo.custom_view.chapter8.Chapter8Activity
import com.hm.viewdemo.custom_view.chapter9.Chapter9Activity

class GetStartAndPracticeActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, GetStartAndPracticeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start_and_practice)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnChapter7 -> {
                Chapter7Activity.launch(this)
            }
            R.id.btnChapter8 -> {
                Chapter8Activity.launch(this)
            }
            R.id.btnChapter9 -> {
                Chapter9Activity.launch(this)
            }
        }
    }
}
