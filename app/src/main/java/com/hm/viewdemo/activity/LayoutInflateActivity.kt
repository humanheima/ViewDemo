package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_layout_inflate.*

/**
 * LayoutInflater源码分析
 */
class LayoutInflateActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LayoutInflateActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_inflate)

        /*val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.activity_bottom_sheet,null)*/
        val button = layoutInflater.inflate(R.layout.button_layout, null)
        clRoot.addView(button)

    }
}
