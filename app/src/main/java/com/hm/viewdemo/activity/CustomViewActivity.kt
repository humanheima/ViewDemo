package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityCustomViewBinding

/**
 * Created by p_dmweidu on 2024/10/22
 * Desc: 测试自定义View
 */
class CustomViewActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, CustomViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityCustomViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.heartBeatEventView.setText("心动事件")
        binding.heartBeatEventView.setProgress(100, 50)

        binding.heartBeatEventView2.setText("心动事件")
        binding.heartBeatEventView2.setProgress(100, 100)


        binding.dailyBindView.setText("每日牵绊")
        binding.dailyBindView.setProgress(10, 5)

        binding.dailyBindView2.setText("每日牵绊")
        binding.dailyBindView2.setProgress(1, 1)

    }
}