package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
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
    }

    private fun renderScriptBlur() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.balloon)
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
        binding.ivBlurry.setOnClickListener {
            Blurry.with(this).capture(binding.ivBlurry).into(binding.ivBlurry)
        }
    }

}