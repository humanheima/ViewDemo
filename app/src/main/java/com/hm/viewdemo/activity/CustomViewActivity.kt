package com.hm.viewdemo.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.databinding.ActivityCustomViewBinding
import org.libpag.PAGFile


/**
 * Created by p_dmweidu on 2024/10/22
 * Desc: 测试自定义View
 */
class CustomViewActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "CustomViewActivity"

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

        binding.ringProgressView.useLinearGradient = false
        binding.ringProgressView2.useLinearGradient = true

        binding.ringProgressView3.colors =
            intArrayOf(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)

        binding.ringProgressView4.colors =
            intArrayOf(Color.RED, Color.GREEN, Color.YELLOW, Color.RED)

        binding.flInnerView.post {
            val viewGroupHeight = binding.flInnerView.height
            val view4Height = binding.ringProgressView4.height

            Log.d(TAG, "onCreate: viewGroupHeight = $viewGroupHeight")
            Log.d(TAG, "onCreate: view4Height = $view4Height")

            binding.flInnerView.requestLayout()

        }

        binding.pagView.setPathAsync("https://imgservices-1252317822.image.myqcloud.com/coco/s09242024/ba99f007.jov7su.pag") { p0 ->
            if (p0 != null) {
                binding.pagView.composition = p0
                binding.pagView.setRepeatCount(0)
                binding.pagView.play()
            }
        }

        binding.flInnerView.setOnClickListener {

            rotateImage()
        }

    }

    private fun rotateImage() {
        // 旋转30度
        val animator = ObjectAnimator.ofFloat(
            binding.pagView,
            "rotation",
            binding.pagView.rotation + 30
        )
        animator.setDuration(300) // 旋转持续时间
        animator.start()
    }

//    private fun rotateImage() {
//        // 旋转30度
//        val animator = ObjectAnimator.ofFloat(
//            binding.ringProgressView4,
//            "rotation",
//            binding.ringProgressView4.getRotation() + 30
//        )
//        animator.setDuration(300) // 旋转持续时间
//        animator.start()
//    }
}