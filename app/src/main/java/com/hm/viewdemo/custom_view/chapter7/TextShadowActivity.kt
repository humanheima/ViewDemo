package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityTextShadowBinding

class TextShadowActivity : BaseActivity<ActivityTextShadowBinding>() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TextShadowActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityTextShadowBinding {
        return ActivityTextShadowBinding.inflate(layoutInflater)
    }

    override fun initData() {
        //通过代码给文字设置阴影
        binding.tv.setShadowLayer(5f, 10f, 10f, resources.getColor(R.color.colorPrimary))
    }

}
