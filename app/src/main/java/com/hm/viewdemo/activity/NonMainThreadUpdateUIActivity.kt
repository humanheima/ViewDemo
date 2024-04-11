package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityNonMainThreadUpdateUiBinding
import kotlinx.android.synthetic.main.activity_non_main_thread_update_ui.tvText
import kotlin.concurrent.thread

/**
 * Crete by dumingwei on 2019-12-14
 * Desc: 测试在非UI线程更新UI
 *
 */
class NonMainThreadUpdateUIActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "NonMainThreadUpdateUIAc"

        fun launch(context: Context) {
            val intent = Intent(context, NonMainThreadUpdateUIActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityNonMainThreadUpdateUiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNonMainThreadUpdateUiBinding.inflate(layoutInflater)
        setContentView(binding.root)


        thread(true) {
            binding.tvText.text = "在子线程更新UI"

            binding.tvText2.text = "在子线程更新UI2"
        }

        binding.btnUpdateUI.setOnClickListener {
            thread(true) {
                Log.i(TAG, "onCreate: current thread is ${Thread.currentThread().name}")
                tvText.text = "在子线程更新UI" +
                        "\n了已经OnResume已经OnResume 了，" +
                        "\n了已经OnResume已经OnResume 了，" +
                        "\n了已经OnResume已经OnResume 了，" +
                        "\n已经OnResume 了已经OnResume " +
                        "\n了已经OnResume 了已经OnResume" +
                        "\n了已经OnResume 了在子线程更新UI"
            }
        }

        binding.btnUpdateUI2.setOnClickListener {
            thread(true) {
                //Note: 这里更新会崩溃
                Log.i(TAG, "onCreate: current thread is ${Thread.currentThread().name}")
                binding.tvText2.text = "在子线程更新UI2" +
                        "\n了已经OnResume已经OnResume 了，" +
                        "\n了已经OnResume已经OnResume 了，" +
                        "\n了已经OnResume已经OnResume 了，" +
                        "\n已经OnResume 了已经OnResume " +
                        "\n了已经OnResume 了已经OnResume" +
                        "\n了已经OnResume 了在子线程更新UI2"
            }
        }
    }

}
