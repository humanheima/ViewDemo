package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.hm.viewdemo.databinding.CharViewDailyBindBinding

/**
 * Created by p_dmweidu on 2024/10/22
 * Desc: 每日牵绊View
 */
class DailyBindView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = CharViewDailyBindBinding.inflate(LayoutInflater.from(context), this)


    fun setText(text: String) {
        binding.tvBottomTitle.text = text
    }

    fun setProgress(maxProgress: Int, progress: Int) {
        binding.tvCenterProgress.text = progress.toString()
        binding.tvTotalProgress.text = maxProgress.toString()
        binding.ringProgressView.maxProgress = maxProgress
        binding.ringProgressView.currentProgress = progress
    }


}