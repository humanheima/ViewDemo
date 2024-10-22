package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.custom_view.chapter7.widget.LinearGradientView
import com.hm.viewdemo.custom_view.chapter7.widget.PlayPageBackgroundView
import com.hm.viewdemo.databinding.ActivityLinearGradientBinding
import com.hm.viewdemo.widget.GradientTextView
import com.hm.viewdemo.widget.LinearGradientView2

class LinearGradientActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "LinearGradientActivity"

        fun launch(context: Context) {
            val intent = Intent(context, LinearGradientActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var lgView1: LinearGradientView

    private lateinit var playPageBgView1: PlayPageBackgroundView
    private lateinit var playPageBgView2: PlayPageBackgroundView
    private lateinit var playPageBgView3: PlayPageBackgroundView

    private lateinit var binding: ActivityLinearGradientBinding

    private lateinit var lg_view_2: LinearGradientView2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinearGradientBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.tvGradient1.direction = GradientTextView.GradientDirection.LEFT_TO_RIGHT
//        binding.tvGradient2.direction = GradientTextView.GradientDirection.RIGHT_TO_LEFT
//        binding.tvGradient3.direction = GradientTextView.GradientDirection.TOP_TO_BOTTOM
//        binding.tvGradient4.direction = GradientTextView.GradientDirection.BOTTOM_TO_TOP

        val colors = intArrayOf(0xFFFF0000.toInt(), 0xFF0000FF.toInt()) // 红到蓝

        binding.tvGradient4.post {
            val width = binding.tvGradient4.width.toFloat()
            val height = binding.tvGradient4.height.toFloat()
            Log.i(TAG, "onCreate: width = $width , height = $height")
            binding.tvGradient4.paint.shader = LinearGradient(
                0f,
                0f,
                width,
                height,
                colors,
                null,
                Shader.TileMode.CLAMP
            )
            binding.tvGradient4.requestLayout()
            binding.tvGradient4.invalidate()

        }
//
        binding.tvGradient5.direction =
            GradientTextView.GradientDirection.LEFT_TO_RIGHT_TOP_TO_BOTTOM
//        binding.tvGradient6.direction =
//            GradientTextView.GradientDirection.RIGHT_TO_LEFT_TOP_TO_BOTTOM
//        binding.tvGradient7.direction =
//            GradientTextView.GradientDirection.LEFT_TO_RIGHT_BOTTOM_TO_TOP
//        binding.tvGradient8.direction =
//            GradientTextView.GradientDirection.RIGHT_TO_LEFT_BOTTOM_TO_TOP


        lg_view_2 = findViewById(R.id.lg_view_2)

//        lg_view_2.setColors(
//            resources.getColor(R.color.colorAccent),
//            resources.getColor(R.color.colorPrimaryDark)
//        )
        lgView1 = findViewById(R.id.lg_view_1)
        playPageBgView1 = findViewById(R.id.play_page_bg_view1)

        playPageBgView2 = findViewById(R.id.play_page_bg_view2)
        playPageBgView2.setShouldDrawGradientLayer(false)

        playPageBgView3 = findViewById(R.id.play_page_bg_view3)

        playPageBgView3.setShouldDrawAlphaLayer(false)
        playPageBgView2.setShouldDrawGradientLayer(false)


    }
}
