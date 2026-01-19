package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityCurvedImageViewBinding
import com.hm.viewdemo.widget.CurvedImageView

/**
 * 展示ImageView弯曲效果的Activity
 */
class CurvedImageViewActivity : BaseActivity<ActivityCurvedImageViewBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CurvedImageViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityCurvedImageViewBinding {
        return ActivityCurvedImageViewBinding.inflate(layoutInflater)
    }

    override fun initData() {
        // 设置SeekBar的监听器来调节弯曲程度
        binding.seekBarCurvature.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 将0-100的进度转换为0-1的弯曲程度
                val curvature = progress / 100f
                binding.curvedImageView.setCurvature(curvature)
                binding.tvCurvatureValue.text = "弯曲程度: ${String.format("%.2f", curvature)}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 设置初始值
        binding.seekBarCurvature.progress = 50 // 初始弯曲程度为0.5
        binding.curvedImageView.setCurvature(0.5f)
        binding.tvCurvatureValue.text = "弯曲程度: 0.50"

        // 设置不同的示例图片按钮
        binding.btnSampleImage1.setOnClickListener {
            binding.curvedImageView.setImageResource(R.drawable.sample_curved_image_1)
        }

        binding.btnSampleImage2.setOnClickListener {
            binding.curvedImageView.setImageResource(R.drawable.sample_curved_image_2)
        }

        // 使用棋盘图案测试弯曲效果
        binding.btnSampleImage3.setOnClickListener {
            binding.curvedImageView.setImageResource(R.drawable.sample_curved_image_3)
        }

        // 添加一个使用现有系统图标的选项
        binding.btnReset.setOnClickListener {
            binding.seekBarCurvature.progress = 0
            binding.curvedImageView.setCurvature(0f)
            binding.tvCurvatureValue.text = "弯曲程度: 0.00"
            // 使用应用图标进行测试
            binding.curvedImageView.setImageResource(R.mipmap.ic_launcher)
        }
    }
}
