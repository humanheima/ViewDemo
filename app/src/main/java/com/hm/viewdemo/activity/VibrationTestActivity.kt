package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Toast
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityVibrationTestBinding

/**
 * Created by GitHub Copilot on 2025/12/23
 * Desc: 震动功能测试Activity
 */
class VibrationTestActivity : BaseActivity<ActivityVibrationTestBinding>() {

    private var vibrator: Vibrator? = null

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, VibrationTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityVibrationTestBinding {
        return ActivityVibrationTestBinding.inflate(layoutInflater)
    }

    override fun initData() {
        initVibrator()
        setupClickListeners()
    }

    private fun initVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private fun setupClickListeners() {
        binding.btnShortVibration.setOnClickListener {
            shortVibration()
        }

        binding.btnLongVibration.setOnClickListener {
            longVibration()
        }

        binding.btnPatternVibration.setOnClickListener {
            patternVibration()
        }

        binding.btnDoubleClickVibration.setOnClickListener {
            doubleClickVibration()
        }

        binding.btnSuccessVibration.setOnClickListener {
            successVibration()
        }

        binding.btnWarningVibration.setOnClickListener {
            warningVibration()
        }

        binding.btnErrorVibration.setOnClickListener {
            errorVibration()
        }

        binding.btnStopVibration.setOnClickListener {
            stopVibration()
        }

        binding.btnCustomPattern.setOnClickListener {
            customPatternVibration()
        }
    }

    /**
     * 短震动 - 50ms
     */
    private fun shortVibration() {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(50)
            }
        }
        showToast("短震动 (50ms)")
    }

    /**
     * 长震动 - 500ms
     */
    private fun longVibration() {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(500)
            }
        }
        showToast("长震动 (500ms)")
    }

    /**
     * 节拍震动 - 短长短长的节拍
     */
    private fun patternVibration() {
        vibrator?.let { vib ->
            val pattern = longArrayOf(0, 100, 100, 200, 100, 100, 100, 300)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(pattern, -1)
            }
        }
        showToast("节拍震动")
    }

    /**
     * 双击震动
     */
    private fun doubleClickVibration() {
        vibrator?.let { vib ->
            val pattern = longArrayOf(0, 50, 100, 50)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(pattern, -1)
            }
        }
        showToast("双击震动")
    }

    /**
     * 成功提示震动 - 使用预定义效果
     */
    private fun successVibration() {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(30)
            }
        }
        showToast("成功提示震动")
    }

    /**
     * 警告震动
     */
    private fun warningVibration() {
        vibrator?.let { vib ->
            val pattern = longArrayOf(0, 100, 50, 100, 50, 100)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(pattern, -1)
            }
        }
        showToast("警告震动")
    }

    /**
     * 错误震动
     */
    private fun errorVibration() {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
            } else {
                val pattern = longArrayOf(0, 300, 100, 300)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vib.vibrate(VibrationEffect.createWaveform(pattern, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vib.vibrate(pattern, -1)
                }
            }
        }
        showToast("错误震动")
    }

    /**
     * 自定义节拍震动
     */
    private fun customPatternVibration() {
        vibrator?.let { vib ->
            // 模拟SOS摩尔斯电码: ... --- ...
            val pattern = longArrayOf(
                0,    // 开始延时
                100, 50, 100, 50, 100, 200,  // ... (短短短)
                300, 50, 300, 50, 300, 200,  // --- (长长长)
                100, 50, 100, 50, 100        // ... (短短短)
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(pattern, -1)
            }
        }
        showToast("SOS 摩尔斯电码震动")
    }

    /**
     * 停止震动
     */
    private fun stopVibration() {
        vibrator?.cancel()
        showToast("停止震动")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 确保Activity销毁时停止震动
        vibrator?.cancel()
    }
}
