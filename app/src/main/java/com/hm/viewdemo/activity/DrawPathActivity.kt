package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityDrawPathBinding

/**
 * Crete by dumingwei on 2019-08-12
 * Desc: 学习Path的基本操作
 * 参考: https://www.gcssloop.com/customview/Path_Basic/
 *
 */
class DrawPathActivity : BaseActivity<ActivityDrawPathBinding>() {

    private var ivAnimatorGradientDrawable: ImageView? = null

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, DrawPathActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityDrawPathBinding {
        return ActivityDrawPathBinding.inflate(layoutInflater)
    }

    override fun initData() {
    }
}
