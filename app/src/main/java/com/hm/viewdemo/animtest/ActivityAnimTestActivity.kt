package com.hm.viewdemo.animtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2023/6/13
 * Desc: 测试Activity 的动画
 */
class ActivityAnimTestActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ActivityAnimTestActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * 从MainActivity跳转到这个Activity
         * MainActivity执行alpha_out动画，这个Activity执行alpha_in动画
         */
        //overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_test)
    }

    override fun finish() {
        super.finish()
        /**
         * 从这个Activity返回MainActivity，这个Activity执行alpha_out动画，MainActivity执行alpha_in动画
         */
        //overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }


}