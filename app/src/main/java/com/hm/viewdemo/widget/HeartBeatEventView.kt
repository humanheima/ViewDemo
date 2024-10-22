package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.hm.viewdemo.databinding.CharViewHeartBeatBinding

/**
 * Created by p_dmweidu on 2024/10/22
 * Desc: 心跳事件View
 */
class HeartBeatEventView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {


    private val binding = CharViewHeartBeatBinding.inflate(LayoutInflater.from(context), this)

    private val progressView2: RingProgressView2 by lazy {
        RingProgressView2(context)
    }

    fun setText(text: String) {
        binding.tvBottomTitle.text = text
    }

    fun setProgress(maxProgress: Int, progress: Int) {
        binding.tvCenterProgress.text = progress.toString()
        binding.ringProgressView.maxProgress = maxProgress
        binding.ringProgressView.currentProgress = progress
    }





}