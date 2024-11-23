package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityTestRotateYactivityBinding
import com.hm.viewdemo.widget.MatrixExample

/**
 * Created by p_dmweidu on 2024/11/19
 * Desc:
 */
class TestRotateYActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, TestRotateYActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityTestRotateYactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestRotateYactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRotate.setOnClickListener {
            //rotate()

            MatrixExample.main()
        }

    }

    private fun rotate() {
        binding.ivLeft.animate()
            .rotationY(-70f) // 向内弯曲的角度
            .scaleX(0.7f)
            .scaleY(0.7f)
            .setDuration(5000) // 动画持续时间
            .withEndAction {
                // 动画结束后的操作，例如恢复到原来的状态
                binding.ivLeft.animate()
                    .rotationY(0f) // 恢复到原来的状态
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(5000)
                    .start()
            }
            .start()

        binding.ivRight.animate()
            .rotationY(70f) // 向内弯曲的角度
            .scaleX(0.7f)
            .scaleY(0.7f)
            .setDuration(5000) // 动画持续时间
            .withEndAction {
                // 动画结束后的操作，例如恢复到原来的状态
                binding.ivRight.animate()
                    .rotationY(0f) // 恢复到原来的状态
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(5000)
                    .start()
            }
            .start()
    }
}