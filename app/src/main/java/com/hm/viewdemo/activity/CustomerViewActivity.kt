package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_custome_view.*

class CustomerViewActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName


    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, CustomerViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custome_view)

        ivHeight.post {
            Log.d(TAG, "onCreate: ${ivHeight.measuredHeight}")
            Log.d(TAG, "onCreate: ${ivHeight.measuredWidth}")
        }
    }


}