package com.hm.viewdemo.custom_view.chapter8

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hm.viewdemo.R

class Chapter8Activity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, Chapter8Activity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter8)
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.btnPorterDuffXfermode -> {
                PorterDuffXfermodeViewActivity.launch(this)
            }
        }
    }
}
