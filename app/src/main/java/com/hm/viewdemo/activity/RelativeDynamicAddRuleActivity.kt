package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.RelativeLayout
import com.hm.viewdemo.databinding.ActivityRelativeDynamicAddRuleBinding

/**
 * Created by dumingwei on 2020/12/30
 *
 * Desc:测试Relative动态改变约束
 */
class RelativeDynamicAddRuleActivity : AppCompatActivity() {


    private lateinit var imageView: ImageView

    private lateinit var binding: ActivityRelativeDynamicAddRuleBinding

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, RelativeDynamicAddRuleActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRelativeDynamicAddRuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.imageView


        binding.btnChangeRule.setOnClickListener {
            val layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams

            //移除老的约束
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)

            //添加新的约束
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            imageView.layoutParams = layoutParams

        }
    }
}