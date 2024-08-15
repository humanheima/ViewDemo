package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityScrollerBinding

/**
 * Created by dumingwei on 2020/4/13
 *
 * Desc:
 */
class ScrollerActivity : BaseActivity<ActivityScrollerBinding>() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ScrollerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityScrollerBinding {
        return ActivityScrollerBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.btnStartScroll.setOnClickListener {
            //向右下方向滚动100像素
            //smoothScrollView.smoothScrollTo(-100, -100)
            binding.smoothScrollView.smoothScrollTo(100, 100)
        }

        binding.btnScroll.setOnClickListener {
            Log.i(TAG, "onCreate: ${binding.smoothScrollView.scrollY}")
        }
    }

}
