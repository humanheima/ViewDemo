package com.hm.viewdemo.custom_view.chapter_12

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R

class Chapter12Activity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, Chapter12Activity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter12)
    }
}
