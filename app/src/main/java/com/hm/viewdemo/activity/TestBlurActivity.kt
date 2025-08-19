package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityTestBlurBinding
import com.hm.viewdemo.util.BlurUtils
import com.hm.viewdemo.util.FastBlur
import jp.wasabeef.blurry.Blurry


/**
 * Created by p_dmweidu on 2024/1/22
 * Desc: 测试高斯模糊
 */
class TestBlurActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTestBlurBinding

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, TestBlurActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBlurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        renderScriptBlur()
        fastBlur()
        useBlurry()
        useBlurry2()
        testOnCreate()

    }

    private fun renderScriptBlur() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_background)
        val blurBitmap = BlurUtils.fastBlur(this, bitmap, 20f)
        binding.ivFirst.setImageBitmap(blurBitmap)
    }

    private fun fastBlur() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.balloon)
        val blurBitmap = FastBlur.doBlur(bitmap, 20, false)
        binding.ivFastBlur.setImageBitmap(blurBitmap)
    }

    private var blurred = false


    /**
     * 测试使用 jp.wasabeef:blurry:4.0.0
     */

    private fun useBlurry() {
        binding.llContent.setOnClickListener {
            if (blurred) {
                Blurry.delete(binding.llContent)
            } else {
                val startMs = System.currentTimeMillis()
                Blurry.with(this)
                    .radius(25)
                    .sampling(2)
                    .async()
                    .animate(500)
                    .onto((binding.llContent) as ViewGroup)
                Log.d(
                    getString(R.string.app_name),
                    "TIME " + (System.currentTimeMillis() - startMs).toString() + "ms"
                )
            }

            blurred = !blurred
        }


    }

    private fun useBlurry2() {
        binding.btnChangeBlur.setOnClickListener {
            Blurry.with(this).capture(binding.ivBlurry).into(binding.ivBlurry)
        }
    }

    private fun testOnCreate() {
        // 检查设备是否支持模糊效果（API 31+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 获取要应用模糊的 View
            val blurView: LinearLayout = binding.blurBackground

            // 创建模糊效果，半径设为 30f（文档建议 20-50 像素）
            val blurEffect = RenderEffect.createBlurEffect(
                20f, // X轴模糊半径
                20f, // Y轴模糊半径
                Shader.TileMode.CLAMP // 边缘处理
            )

            // 初始应用模糊效果
            //blurView.setRenderEffect(blurEffect)

            // 按钮切换模糊效果
            val toggleButton = binding.toggleBlurButton
            toggleButton.setOnClickListener {
                blurView.setRenderEffect(blurEffect)
//                if (blurView == null) {
//                    blurView.setRenderEffect(blurEffect)
//                    toggleButton.text = "关闭模糊"
//                } else {
//                    blurView.setRenderEffect(null)
//                    toggleButton.text = "开启模糊"
//                }
            }
        } else {
            // 对于不支持的设备，隐藏按钮并设置默认背景
            //findViewById<Button>(R.id.toggleBlurButton).visibility = View.GONE
            findViewById<LinearLayout>(R.id.toggleBlurButton).setBackgroundResource(android.R.color.darker_gray)
        }
    }
}