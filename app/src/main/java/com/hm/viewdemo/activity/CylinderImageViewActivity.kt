package com.hm.viewdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.CylinderImageView

/**
 * 演示 CylinderImageView：图片向屏幕内弯曲（圆柱前半部分）的效果。
 * - 拖动 SeekBar 调弯曲角度 / 透视强度
 * - Switch 切换弯曲轴向（左右弯 / 上下弯）
 * - 按钮开关“呼吸”动画
 */
class CylinderImageViewActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, CylinderImageViewActivity::class.java))
        }
    }

    private var animator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cylinder_image_view)

        val cylinder = findViewById<CylinderImageView>(R.id.cylinder)
        val tvArc = findViewById<TextView>(R.id.tvArc)
        val tvCam = findViewById<TextView>(R.id.tvCam)
        val seekArc = findViewById<SeekBar>(R.id.seekArc)
        val seekCam = findViewById<SeekBar>(R.id.seekCam)
        val switchAxis = findViewById<Switch>(R.id.switchAxis)
        val btnAnim = findViewById<Button>(R.id.btnAnim)

        seekArc.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                stopAnim(btnAnim)
                cylinder.arcDegrees = value.toFloat()
                tvArc.text = "弯曲角度 arcDegrees: $value"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) = Unit
            override fun onStopTrackingTouch(sb: SeekBar?) = Unit
        })

        seekCam.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                // 进度 1~40 映射到 0.1~4.0
                val factor = (value.coerceAtLeast(1)) / 10f
                cylinder.cameraFactor = factor
                tvCam.text = "透视强度 cameraFactor: $factor"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) = Unit
            override fun onStopTrackingTouch(sb: SeekBar?) = Unit
        })

        switchAxis.setOnCheckedChangeListener { _, checked ->
            cylinder.horizontalAxis = checked
        }

        btnAnim.setOnClickListener {
            if (animator?.isRunning == true) {
                stopAnim(btnAnim)
            } else {
                startAnim(cylinder, tvArc, seekArc, btnAnim)
            }
        }
    }

    private fun startAnim(
        cylinder: CylinderImageView, tvArc: TextView, seekArc: SeekBar, btn: Button
    ) {
        animator = ValueAnimator.ofFloat(0f, 130f).apply {
            duration = 1500
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val v = it.animatedValue as Float
                cylinder.arcDegrees = v
                tvArc.text = "弯曲角度 arcDegrees: ${v.toInt()}"
                seekArc.progress = v.toInt()
            }
            start()
        }
        btn.text = "停止动画"
    }

    private fun stopAnim(btn: Button) {
        animator?.cancel()
        animator = null
        btn.text = "呼吸动画"
    }

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()
        animator = null
    }
}
