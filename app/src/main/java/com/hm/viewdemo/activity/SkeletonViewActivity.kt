package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2021/8/24
 *
 * Desc: 
 */
class SkeletonViewActivity : AppCompatActivity() {

    companion object {
    
        fun launch(context: Context) {
            val intent = Intent(context, SkeletonViewActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skeleton_view)
    }
}