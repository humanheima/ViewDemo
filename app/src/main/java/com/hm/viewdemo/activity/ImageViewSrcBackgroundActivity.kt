package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_image_view_src_background.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view_src_background)

        ivBackgroundPadding.post {
            Log.d(TAG, "onCreate: ivBackgroundPadding  ${ivBackgroundPadding.left},${ivBackgroundPadding.right}")

        }
        Log.d(TAG, "onCreate: ${ivSrcPadding.drawable.javaClass}")
        ivSrcPadding.post {
            Log.d(TAG, "onCreate: ivSrcPadding ${ivSrcPadding.left},${ivSrcPadding.right}")
        }

        btnChangeBackground.setOnClickListener {
            ivBackgroundPadding.background = resources.getDrawable(R.drawable.avatar)
            ivBackgroundPadding.setPadding(40, 40, 40, 40)
        }
    }
}
