package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityImageViewSrcBackgroundBinding

/**
 * Crete by dumingwei on 2019-09-26
 * Desc:
 * 1. 测试ImageView src 和 background 的区别
 * 2. 测试 padding 对 background 的影响
 *
 */
class ImageViewSrcBackgroundActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ImageViewSrcBackgroundActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val TAG = "ImageViewSrcBackgroundA"

    private lateinit var binding: ActivityImageViewSrcBackgroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view_src_background)

        binding.ivBackgroundPadding.post {
            Log.i(
                TAG,
                "onCreate: ivBackgroundPadding  ${binding.ivBackgroundPadding.left},${binding.ivBackgroundPadding.right}"
            )

        }
        Log.i(TAG, "onCreate: ${binding.ivSrcPadding.drawable.javaClass}")
        binding.ivSrcPadding.post {
            Log.i(
                TAG,
                "onCreate: ivSrcPadding ${binding.ivSrcPadding.left},${binding.ivSrcPadding.right}"
            )
        }

        binding.btnChangeBackground.setOnClickListener {
            binding.ivBackgroundPadding.background =
                resources.getDrawable(R.drawable.ic_soft_avatar)
            binding.ivBackgroundPadding.setPadding(40, 40, 40, 40)
        }

    }

}
