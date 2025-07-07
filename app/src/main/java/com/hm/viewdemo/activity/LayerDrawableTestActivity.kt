package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityLayerDrawableTestBinding
import com.hm.viewdemo.extension.dp2px
import com.hm.viewdemo.extension.dp2pxFloat

/**
 * Created by p_dmweidu on 2025/7/7
 * Desc: 测试 LayerDrawable
 */
class LayerDrawableTestActivity : AppCompatActivity() {

    private val TAG = "LayerDrawableTestActivi"

    private lateinit var binding: ActivityLayerDrawableTestBinding

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LayerDrawableTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayerDrawableTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createLayerDrawable()
    }

    private fun createLayerDrawable() {
        val dp10 = 10.dp2px(this@LayerDrawableTestActivity)
        val dp10f = 10.dp2pxFloat(this@LayerDrawableTestActivity)
        val layers = arrayOf(
            // 第一层：红色背景
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(Color.RED)
                cornerRadius = dp10f
            },
            // 第二层：白色前景
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(Color.WHITE)
                cornerRadius = 5.dp2pxFloat(this@LayerDrawableTestActivity)
            }
        )

        val layerDrawable = LayerDrawable(layers)
        // 设置第二层的内边距,index = 1
        layerDrawable.setLayerInset(1, dp10, dp10, dp10, dp10)

        // 应用到 View
        binding.tv4.background = layerDrawable

    }


}