package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.RelativeLayout
import com.hm.viewdemo.databinding.ActivityRelativeDynamicAddRuleBinding
import com.hm.viewdemo.util.SpUtil
import com.hm.viewdemo.widget.AdsorbView

/**
 * Created by dumingwei on 2020/12/30
 *
 * Desc:测试Relative动态改变约束
 */
class RelativeDynamicAddRuleActivity : AppCompatActivity() {

    private val TAG: String = "RelativeDynamicAddRuleA"

    private lateinit var adsorbView: AdsorbView

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

        adsorbView = binding.adsorbView

        adsorbView.addMoveDirectionListener { isRight ->
            saveBottomMarginToLocal(isRight)
        }

        binding.btnChangeRule.setOnClickListener {
            val layoutParams = adsorbView.layoutParams as RelativeLayout.LayoutParams

            //移除老的约束
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)

            //添加新的约束
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            adsorbView.layoutParams = layoutParams

        }

        binding.btnSaveBottomMargin.setOnClickListener {
            //saveBottomMarginToLocal()
        }

        binding.btnLoadBottomMargin.setOnClickListener {
            loadBottomMarginFromLocal()
        }
    }

    private fun saveBottomMarginToLocal(isRight: Boolean) {
        val layoutParams = binding.adsorbView.layoutParams as RelativeLayout.LayoutParams
        val bottomMargin = layoutParams.bottomMargin
        Log.d(TAG, "saveBottomMarginToLocal: bottom margin = $bottomMargin")
        SpUtil.getInstance(this).saveInt("bottom_margin", bottomMargin)
        SpUtil.getInstance(this).saveBoolean("is_right", isRight)
    }

    private fun loadBottomMarginFromLocal() {
        val layoutParams = binding.adsorbView.layoutParams as RelativeLayout.LayoutParams
        val bottomMargin = SpUtil.getInstance(this).getInt("bottom_margin")
        Log.d(TAG, "loadBottomMarginFromLocal: bottom margin = $bottomMargin")
        val isRight = SpUtil.getInstance(this).getBoolean("is_right")

        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        if (isRight) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        }
        layoutParams.bottomMargin = bottomMargin
    }

}