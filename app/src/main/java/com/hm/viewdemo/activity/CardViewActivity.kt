package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-01
 * Desc:
 *
 * 参考链接：https://www.jianshu.com/p/b105019028b6
 */
class CardViewActivity : AppCompatActivity() {


    companion object {

        private val TAG: String = "CardViewActivity"

        private val COS_45 = Math.cos(Math.toRadians(45.0))

        fun launch(context: Context) {
            val intent = Intent(context, CardViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view)
        Log.i(TAG, "onCreate: COS_45 = $COS_45")
    }


}
