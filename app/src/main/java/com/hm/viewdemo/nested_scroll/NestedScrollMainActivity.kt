package com.hm.viewdemo.nested_scroll

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hm.viewdemo.R

class NestedScrollMainActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, NestedScrollMainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_main)
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.btnNestedScroll_1 -> {
                NestedScrollVersionOneActivity.launch(this)
            }
            R.id.btnNestedScroll_2 -> {
                NestedScrollVersion2Activity.launch(this)

            }
        }
    }
}