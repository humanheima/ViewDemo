package com.hm.viewdemo.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.WaveProgressView

/**
 * 演示 WaveProgressView：水波进度球。
 * - 拖动 SeekBar 调进度 / 波振幅 / 波密度
 * - “自动加载到100%”模拟进度上涨
 * - “停止水流 / 开始水流”开关水面流动动画
 */
class WaveProgressViewActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, WaveProgressViewActivity::class.java))
        }
    }

    private var fillAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave_progress_view)

        val wave = findViewById<WaveProgressView>(R.id.wave)
        val tvProgress = findViewById<TextView>(R.id.tvProgress)
        val tvAmp = findViewById<TextView>(R.id.tvAmp)
        val tvWaveCount = findViewById<TextView>(R.id.tvWaveCount)
        val seekProgress = findViewById<SeekBar>(R.id.seekProgress)
        val seekAmp = findViewById<SeekBar>(R.id.seekAmp)
        val seekWaveCount = findViewById<SeekBar>(R.id.seekWaveCount)
        val btnAutoFill = findViewById<Button>(R.id.btnAutoFill)
        val btnWaveAnim = findViewById<Button>(R.id.btnWaveAnim)

        wave.startAnimation()

        seekProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                if (fromUser) stopFill()
                wave.progress = value.toFloat()
                tvProgress.text = "进度 progress: $value"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) = Unit
            override fun onStopTrackingTouch(sb: SeekBar?) = Unit
        })

        seekAmp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                // 进度 0~30 映射到 0~0.30
                val ratio = value / 100f
                wave.amplitudeRatio = ratio
                tvAmp.text = "振幅 amplitudeRatio: $ratio"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) = Unit
            override fun onStopTrackingTouch(sb: SeekBar?) = Unit
        })

        seekWaveCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, value: Int, fromUser: Boolean) {
                // 进度 1~50 映射到 0.1~5.0
                val count = value.coerceAtLeast(1) / 10f
                wave.waveCount = count
                tvWaveCount.text = "波密度 waveCount: $count"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) = Unit
            override fun onStopTrackingTouch(sb: SeekBar?) = Unit
        })

        btnAutoFill.setOnClickListener {
            startFill(wave, tvProgress, seekProgress)
        }

        btnWaveAnim.setOnClickListener {
            if (wave.isFlowing()) {
                wave.stopAnimation()
                btnWaveAnim.text = "开始水流"
            } else {
                wave.startAnimation()
                btnWaveAnim.text = "停止水流"
            }
        }
    }

    private fun startFill(wave: WaveProgressView, tv: TextView, seek: SeekBar) {
        stopFill()
        fillAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 3000
            addUpdateListener {
                val v = it.animatedValue as Float
                wave.progress = v
                tv.text = "进度 progress: ${v.toInt()}"
                seek.progress = v.toInt()
            }
            start()
        }
    }

    private fun stopFill() {
        fillAnimator?.cancel()
        fillAnimator = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopFill()
    }
}
