package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2022/6/1
 * Desc: 测试VectorDrawable
 * https://developer.android.com/reference/android/graphics/drawable/VectorDrawable.html
 */
class VectorDrawableMainActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, VectorDrawableMainActivity::class.java)
            context.startActivity(starter)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector_drawable_main)
    }
}