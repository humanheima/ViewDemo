package com.hm.viewdemo.custom_view.chapter_11

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R

class Chapter11Activity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, Chapter11Activity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter11)
    }
}
