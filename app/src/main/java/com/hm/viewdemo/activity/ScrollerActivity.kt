package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_scroller.*

class ScrollerActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    internal var i = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroller)
        Log.d(TAG, "onCreate: " + (btnScroll!!.text.toString()))
        Log.d(TAG, "onCreate: " + (btnScroll!!.text.toString() == ""))
        btnStartScroll!!.setOnClickListener {
            //testLayout.scrollBy(-20, -20);
            //testLayout!!.scrollBy(20, 20)
            btnButton.smoothScrollTo(100, 100)
        }

    }

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ScrollerActivity::class.java)
            context.startActivity(intent)
        }
    }
}
