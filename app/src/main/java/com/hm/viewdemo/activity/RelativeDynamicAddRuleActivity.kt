package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityRelativeDynamicAddRuleBinding
import com.hm.viewdemo.dp2px
import com.hm.viewdemo.util.ScreenUtil
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

        Log.d(TAG, "onCreate: dp36 = ${36.dp2px(this)}")

        adsorbView = binding.adsorbView

        val screenWidth = ScreenUtil.getScreenWidth(this)
        adsorbView.setDragRect(
            Rect(
                0,
                60.dp2px(this),
                screenWidth,
                ScreenUtil.getScreenHeight(this) - 60.dp2px(this)
            )
        )

        adsorbView.addMoveDirectionListener(object : AdsorbView.IMoveDirection {
            override fun moveDirection(isRight: Boolean) {
                saveBottomMarginToLocal(isRight)
            }
        })

        adsorbView.post {
            adsorbView.setInitialSuction(true, 16.dp2px(this))
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
            saveBottomMarginToLocal(true)
        }

        binding.btnLoadBottomMargin.setOnClickListener {
            loadBottomMarginFromLocal()
        }
    }

    /**
     * 这里的bottomMargin是相对于父布局的，不会变的。一直是布局文件里的 48dp
     */
    private fun saveBottomMarginToLocal(isRight: Boolean) {
        val layoutParams = binding.adsorbView.layoutParams as RelativeLayout.LayoutParams
        val bottomMargin = layoutParams.bottomMargin
        val dynamicBottomMargin = binding.adsorbView.getBottomMargin()
        Log.d(TAG, "saveBottomMarginToLocal: bottom margin = $bottomMargin")

        Log.d(TAG, "saveBottomMarginToLocal: 48dp = ${48.dp2px(this)}")
        Log.d(TAG, "saveBottomMarginToLocal: dynamicBottomMargin = $dynamicBottomMargin")

        SpUtil.getInstance(this).saveInt("bottom_margin", dynamicBottomMargin)
        SpUtil.getInstance(this).saveBoolean("is_right", isRight)
    }

    private fun loadBottomMarginFromLocal() {
        val layoutParams = binding.adsorbView.layoutParams as RelativeLayout.LayoutParams
        val savedBottomMargin = SpUtil.getInstance(this).getInt("bottom_margin")
        val isRight = SpUtil.getInstance(this).getBoolean("is_right")

        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        if (isRight) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        }
        val currentBottomMargin = layoutParams.bottomMargin

        Log.d(
            TAG,
            "loadBottomMarginFromLocal: savedBottomMargin = $savedBottomMargin , current bottom margin = $currentBottomMargin"
        )

        layoutParams.bottomMargin = savedBottomMargin + currentBottomMargin

        binding.adsorbView.layoutParams = layoutParams
    }

}