package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityCustomeViewBinding

class CustomerViewActivity : BaseActivity<ActivityCustomeViewBinding>() {


    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, CustomerViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityCustomeViewBinding {
        return ActivityCustomeViewBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.ivHeight.post {
            Log.i(TAG, "onCreate: ${binding.ivHeight.measuredHeight}")
            Log.i(TAG, "onCreate: ${binding.ivHeight.measuredWidth}")
        }
    }


}