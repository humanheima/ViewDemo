package com.hm.viewdemo.aibot.voice

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ViewVoiceRecordingOverlayBinding
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

/**
 * 录音过程中的浮层 UI：波形动画、计时、中间识别文本、取消/识别中提示。
 *
 * 被 [VoiceInputController] add 到 decorView 上全屏展示。
 */
class VoiceRecordingOverlay @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(ctx, attrs, defStyleAttr) {

    private val binding: ViewVoiceRecordingOverlayBinding =
        ViewVoiceRecordingOverlayBinding.inflate(LayoutInflater.from(ctx), this, true)

    private val waveView: LottieAnimationView = binding.lavVoiceWave
    private val tvTimer: TextView = binding.tvVoiceTimer
    private val tvPartial: TextView = binding.tvVoicePartial
    private val tvHint: TextView = binding.tvVoiceHint

    private val handler = Handler(Looper.getMainLooper())
    private var startMs = 0L
    private val timerRunnable = object : Runnable {
        override fun run() {
            val sec = (System.currentTimeMillis() - startMs) / 1000L
            val mm = (sec / 60).toString().padStart(2, '0')
            val ss = (sec % 60).toString().padStart(2, '0')
            tvTimer.text = String.format(Locale.US, "%s:%s", mm, ss)
            handler.postDelayed(this, 500)
        }
    }

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        startMs = System.currentTimeMillis()
        handler.post(timerRunnable)
    }

    /** 设置音量 dB，范围 -60..0。 */
    fun updateVolume(db: Float) {
        // -60..0 → 0..1 → 控制 Lottie 播放速度 + 缩放
        val ratio = min(1f, max(0f, (db + 60f) / 60f))
        waveView.speed = 0.6f + ratio * 2.0f
        val scale = 0.9f + ratio * 0.35f
        waveView.scaleX = scale
        waveView.scaleY = scale
    }

    fun setCancelHint(inCancel: Boolean) {
        if (inCancel) {
            tvHint.text = "松手取消"
            tvHint.setTextColor(Color.parseColor("#FFFF6B6B"))
        } else {
            tvHint.text = "上滑取消"
            tvHint.setTextColor(Color.parseColor("#CCFFFFFF"))
        }
    }

    fun updatePartialText(text: String) {
        tvPartial.text = text
    }

    fun showRecognizing() {
        handler.removeCallbacks(timerRunnable)
        tvHint.text = "识别中..."
        tvHint.setTextColor(Color.parseColor("#CCFFFFFF"))
    }

    fun showError(msg: String) {
        handler.removeCallbacks(timerRunnable)
        tvHint.text = "出错: $msg"
        tvHint.setTextColor(Color.parseColor("#FFFF6B6B"))
    }

    override fun onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }
}
