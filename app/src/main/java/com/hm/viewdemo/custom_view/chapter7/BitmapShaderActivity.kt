package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityBitmapShaderBinding

class BitmapShaderActivity : BaseActivity<ActivityBitmapShaderBinding>() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BitmapShaderActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityBitmapShaderBinding {
        return ActivityBitmapShaderBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.view5.post {
            Log.i(TAG, "onCreate:${binding.view5.width}, ${binding.view5.height}")
        }
    }
}
