package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityThreeDotProgressBarBinding

/**
 * Created by dumingwei on 2020/4/15
 *
 *
 * Desc: 测试loading dialog progress等
 * 参考链接：https://blog.csdn.net/mad1989/article/details/38042875
 */
class ThreeDotProgressBarActivity : BaseActivity<ActivityThreeDotProgressBarBinding>() {

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, ThreeDotProgressBarActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityThreeDotProgressBarBinding {
        return ActivityThreeDotProgressBarBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.dotProgressBarUseSmallest.enableAnimation = false
        binding.dotProgressBarUseBiggest.enableAnimation = false
        binding.dotProgressBarUseBiggest.debugDrawBiggest = true


        binding.btnChangeColor.setOnClickListener {
            binding.dotProgressBarUse.changeColor(ContextCompat.getColor(this, R.color.colorAccent))
        }
    }


}
